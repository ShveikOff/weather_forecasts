package com.example.weatherforecast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notifications(private val context: Context) {

    companion object {
        private const val WEATHER_CHANNEL_ID = "weather_alerts"
        private const val DAILY_RECOMMENDATIONS_CHANNEL_ID = "daily_recommendations"
        private const val UPDATE_REMINDERS_CHANNEL_ID = "update_reminders"
    }

    init {
        createNotificationChannels()
    }

    // Создание каналов уведомлений (для Android 8+)
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    WEATHER_CHANNEL_ID,
                    "Weather Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Предупреждения о погоде"
                },
                NotificationChannel(
                    DAILY_RECOMMENDATIONS_CHANNEL_ID,
                    "Daily Recommendations",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Ежедневные рекомендации"
                },
                NotificationChannel(
                    UPDATE_REMINDERS_CHANNEL_ID,
                    "Update Reminders",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Напоминания об обновлениях приложения"
                }
            )

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channels.forEach { channel -> notificationManager.createNotificationChannel(channel) }
        }
    }

    // Метод для отправки уведомлений
    private fun sendNotification(channelId: String, title: String, message: String, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Иконка приложения
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    // Предупреждение о погоде
    fun sendWeatherAlertNotification(title: String, message: String) {
        sendNotification(WEATHER_CHANNEL_ID, title, message, 1)
    }

    // Ежедневные рекомендации
    fun sendDailyRecommendationsNotification(title: String, message: String) {
        sendNotification(DAILY_RECOMMENDATIONS_CHANNEL_ID, title, message, 2)
    }

    // Напоминание об обновлении
    fun sendUpdateReminderNotification(title: String, message: String) {
        sendNotification(UPDATE_REMINDERS_CHANNEL_ID, title, message, 3)
    }
}
