package com.example.composetutorial.screens

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class LightSensorManager(private val context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    private var lastBrightness: Float? = null
    private val brightnessThreshold = 1f //   Lowered threshold for better sensitivity

    fun registerListener() {
        if (lightSensor == null) {
            Log.e("LightSensorManager", "❌ No light sensor found on this device!") //   Log if no sensor is found
            return
        }
        Log.d("LightSensorManager", "   Light sensor registered!") //   Log sensor registration
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        Log.d("LightSensorManager", "⏸ Light sensor unregistered") //   Log sensor unregistration
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val brightness = it.values[0] // Get brightness level in lux
            Log.d("LightSensorManager", "🌞 Light Level Changed: $brightness lux") //   Log brightness changes

            // Send notification only if brightness change exceeds the threshold
            if (lastBrightness == null || kotlin.math.abs(brightness - lastBrightness!!) >= brightnessThreshold) {
                if (hasNotificationPermission()) { //   Check permission before triggering notification
                    showNotification("Light Sensor Alert", "Brightness: $brightness lux")
                    Log.d("LightSensorManager", "📢 Notification Triggered!") //   Log when notification is triggered
                } else {
                    Log.e("LightSensorManager", "❌ Notification permission not granted!") //   Log if permission missing
                }
                lastBrightness = brightness
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun showNotification(title: String, message: String) {
        val channelId = "light_sensor_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (!hasNotificationPermission()) { //   Prevent sending notifications if permission is missing
            Log.e("LightSensorManager", "❌ Cannot send notification, permission not granted!")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Light Sensor Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun hasNotificationPermission(): Boolean { //   Check if app has notification permission
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Older Android versions don't require explicit permission
        }
    }
}
