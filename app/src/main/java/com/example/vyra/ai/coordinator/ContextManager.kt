package com.example.vyra.ai.coordinator

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Context manager for sharing context across multiple AI agents
 */
class ContextManager {
    
    private val _sharedContext = MutableStateFlow<Map<String, Any>>(emptyMap())
    val sharedContext = _sharedContext.asStateFlow()
    
    private val _agentContexts = MutableStateFlow<Map<String, Map<String, Any>>>(emptyMap())
    val agentContexts = _agentContexts.asStateFlow()
    
    /**
     * Update shared context
     */
    fun updateSharedContext(updates: Map<String, Any>) {
        val current = _sharedContext.value.toMutableMap()
        current.putAll(updates)
        _sharedContext.value = current
    }
    
    /**
     * Update agent-specific context
     */
    fun updateAgentContext(agentId: String, updates: Map<String, Any>) {
        val current = _agentContexts.value.toMutableMap()
        val agentContext = current[agentId]?.toMutableMap() ?: mutableMapOf()
        agentContext.putAll(updates)
        current[agentId] = agentContext
        _agentContexts.value = current
    }
    
    /**
     * Get context for specific agent
     */
    fun getAgentContext(agentId: String): Map<String, Any> {
        val agentContext = _agentContexts.value[agentId] ?: emptyMap()
        return _sharedContext.value + agentContext
    }
    
    /**
     * Update context based on agent response
     */
    fun updateContext(currentContext: Map<String, Any>, response: AgentResponse): Map<String, Any> {
        val updated = currentContext.toMutableMap()
        
        // Add response metadata to context
        updated["last_agent"] = response.agentId
        updated["last_response"] = response.response
        updated["last_confidence"] = response.confidence
        updated["last_timestamp"] = response.timestamp
        
        // Add custom metadata
        response.metadata.forEach { (key, value) ->
            updated["${response.agentId}_$key"] = value
        }
        
        return updated
    }
    
    /**
     * Clear all contexts
     */
    fun clearAllContexts() {
        _sharedContext.value = emptyMap()
        _agentContexts.value = emptyMap()
    }
    
    /**
     * Clear agent-specific context
     */
    fun clearAgentContext(agentId: String) {
        val current = _agentContexts.value.toMutableMap()
        current.remove(agentId)
        _agentContexts.value = current
    }
}
