package com.example.vyra.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.vyra.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ElevenLabsAudioService : Service() {

    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentTrackName = MutableStateFlow("ElevenLabs Voice Stream")
    val currentTrackName: StateFlow<String> = _currentTrackName

    inner class LocalBinder : Binder() {
        fun getService(): ElevenLabsAudioService = this@ElevenLabsAudioService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val title = intent.getStringExtra(EXTRA_TITLE) ?: "ElevenLabs Audio Stream"
                playAudioStream(title)
            }
            ACTION_PAUSE -> pauseAudioStream()
            ACTION_STOP -> stopAudioStream()
        }
        return START_STICKY
    }

    fun playAudioStream(title: String = "ElevenLabs Cyberpunk Synthesizer") {
        _currentTrackName.value = title
        _isPlaying.value = true

        startForeground(NOTIFICATION_ID, buildNotification(title, "Streaming active in background"))

        // Simulate background continuous audio playback
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun pauseAudioStream() {
        _isPlaying.value = false
        mediaPlayer?.pause()
        startForeground(NOTIFICATION_ID, buildNotification(_currentTrackName.value, "Stream paused"))
    }

    fun stopAudioStream() {
        _isPlaying.value = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "ElevenLabs Audio Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Background streaming service for ElevenLabs Voice AI"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(title: String, statusText: String) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(statusText)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setOngoing(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    companion object {
        const val CHANNEL_ID = "vyra_elevenlabs_audio"
        const val NOTIFICATION_ID = 1001

        const val ACTION_PLAY = "com.example.vyra.action.PLAY"
        const val ACTION_PAUSE = "com.example.vyra.action.PAUSE"
        const val ACTION_STOP = "com.example.vyra.action.STOP"
        const val EXTRA_TITLE = "extra_title"

        fun startPlay(context: Context, title: String) {
            val intent = Intent(context, ElevenLabsAudioService::class.java).apply {
                action = ACTION_PLAY
                putExtra(EXTRA_TITLE, title)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, ElevenLabsAudioService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
