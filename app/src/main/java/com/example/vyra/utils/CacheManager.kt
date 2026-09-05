package com.example.vyra.utils

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

/**
 * Performance optimization and caching manager
 * Handles in-memory caching, disk caching, and cache invalidation strategies
 */
class CacheManager(context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    
    // In-memory cache with LRU eviction
    private val memoryCache = mutableMapOf<String, CacheEntry>()
    private val maxMemoryCacheSize = 50
    private val cacheAccessOrder = mutableListOf<String>()
    
    private val _cacheStats = MutableStateFlow(CacheStats(0, 0, 0, 0))
    val cacheStats = _cacheStats.asStateFlow()
    
    /**
     * Get value from cache
     */
    fun <T> get(key: String, clazz: Class<T>): T? {
        val entry = memoryCache[key]
        
        if (entry != null && !entry.isExpired()) {
            // Update access order
            cacheAccessOrder.remove(key)
            cacheAccessOrder.add(key)
            
            _cacheStats.value = _cacheStats.value.copy(hits = _cacheStats.value.hits + 1)
            
            @Suppress("UNCHECKED_CAST")
            return json.decodeFromString(clazz, entry.data) as T
        }
        
        _cacheStats.value = _cacheStats.value.copy(misses = _cacheStats.value.misses + 1)
        return null
    }
    
    /**
     * Put value in cache
     */
    fun <T> put(key: String, value: T, ttl: Long = TimeUnit.MINUTES.toMillis(5)) {
        val data = json.encodeToString(value)
        val entry = CacheEntry(
            data = data,
            timestamp = System.currentTimeMillis(),
            ttl = ttl
        )
        
        // Evict if cache is full
        if (memoryCache.size >= maxMemoryCacheSize) {
            val oldestKey = cacheAccessOrder.removeFirst()
            memoryCache.remove(oldestKey)
            _cacheStats.value = _cacheStats.value.copy(evictions = _cacheStats.value.evictions + 1)
        }
        
        memoryCache[key] = entry
        cacheAccessOrder.add(key)
        _cacheStats.value = _cacheStats.value.copy(size = memoryCache.size)
    }
    
    /**
     * Invalidate cache entry
     */
    fun invalidate(key: String) {
        memoryCache.remove(key)
        cacheAccessOrder.remove(key)
        _cacheStats.value = _cacheStats.value.copy(size = memoryCache.size)
    }
    
    /**
     * Invalidate all cache entries matching pattern
     */
    fun invalidatePattern(pattern: String) {
        val regex = Regex(pattern)
        val keysToRemove = memoryCache.keys.filter { regex.matches(it) }
        
        keysToRemove.forEach { key ->
            memoryCache.remove(key)
            cacheAccessOrder.remove(key)
        }
        
        _cacheStats.value = _cacheStats.value.copy(size = memoryCache.size)
    }
    
    /**
     * Clear entire cache
     */
    fun clear() {
        memoryCache.clear()
        cacheAccessOrder.clear()
        _cacheStats.value = _cacheStats.value.copy(size = 0)
    }
    
    /**
     * Get cache statistics
     */
    fun getStats(): CacheStats {
        return _cacheStats.value
    }
    
    /**
     * Preload cache with data
     */
    fun <T> preload(data: Map<String, T>, ttl: Long = TimeUnit.MINUTES.toMillis(5)) {
        data.forEach { (key, value) ->
            put(key, value, ttl)
        }
    }
}

@Serializable
data class CacheEntry(
    val data: String,
    val timestamp: Long,
    val ttl: Long
) {
    fun isExpired(): Boolean {
        return System.currentTimeMillis() > timestamp + ttl
    }
}

@Serializable
data class CacheStats(
    val size: Int,
    val hits: Int,
    val misses: Int,
    val evictions: Int
) {
    val hitRate: Double
        get() = if (hits + misses > 0) {
            hits.toDouble() / (hits + misses).toDouble()
        } else {
            0.0
        }
}

/**
 * Disk cache implementation using Room
 */
class DiskCacheManager(context: Context) {
    private val database = Room.databaseBuilder(
        context,
        CacheDatabase::class.java,
        "vyra_cache"
    ).build()
    
    suspend fun <T> get(key: String, clazz: Class<T>): T? {
        val entry = database.cacheDao().getByKey(key)
        if (entry != null && !entry.isExpired()) {
            @Suppress("UNCHECKED_CAST")
            return Json.decodeFromString(clazz, entry.data) as T
        }
        return null
    }
    
    suspend fun <T> put(key: String, value: T, ttl: Long = TimeUnit.HOURS.toMillis(1)) {
        val data = Json.encodeToString(value)
        val entry = CacheEntity(
            key = key,
            data = data,
            timestamp = System.currentTimeMillis(),
            ttl = ttl
        )
        database.cacheDao().insert(entry)
    }
    
    suspend fun invalidate(key: String) {
        database.cacheDao().deleteByKey(key)
    }
    
    suspend fun clear() {
        database.cacheDao().clear()
    }
    
    suspend fun cleanupExpired() {
        database.cacheDao().deleteExpired(System.currentTimeMillis())
    }
}

/**
 * Room database for disk caching
 */
@androidx.room.Entity(tableName = "cache")
@Serializable
data class CacheEntity(
    @androidx.room.PrimaryKey val key: String,
    val data: String,
    val timestamp: Long,
    val ttl: Long
) {
    fun isExpired(): Boolean {
        return System.currentTimeMillis() > timestamp + ttl
    }
}

@androidx.room.Dao
interface CacheDao {
    @androidx.room.Query("SELECT * FROM cache WHERE key = :key")
    suspend fun getByKey(key: String): CacheEntity?
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(entry: CacheEntity)
    
    @androidx.room.Query("DELETE FROM cache WHERE key = :key")
    suspend fun deleteByKey(key: String)
    
    @androidx.room.Query("DELETE FROM cache")
    suspend fun clear()
    
    @androidx.room.Query("DELETE FROM cache WHERE timestamp + ttl < :currentTime")
    suspend fun deleteExpired(currentTime: Long)
}

@androidx.room.Database(
    entities = [CacheEntity::class],
    version = 1
)
abstract class CacheDatabase : androidx.room.RoomDatabase() {
    abstract fun cacheDao(): CacheDao
}
