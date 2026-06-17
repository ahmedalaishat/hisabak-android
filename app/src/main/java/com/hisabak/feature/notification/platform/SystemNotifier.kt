package com.hisabak.feature.notification.platform

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.hisabak.MainActivity
import com.hisabak.R
import com.hisabak.feature.notification.domain.Notification
import com.hisabak.feature.notification.domain.Notifier

/** Posts Android system notifications for budget alerts. A no-op (the in-app record still
 *  exists) when the runtime permission isn't granted. */
class SystemNotifier(private val context: Context) : Notifier {

    fun ensureChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Budget alerts",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply { description = "Alerts when a category nears or exceeds its monthly limit" }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun post(notification: Notification) {
        if (!hasPermission()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_CATEGORY_ID, notification.categoryId)
        }
        // One OS-notification id per category so a category's later alerts replace its earlier one.
        val id = notification.categoryId?.hashCode() ?: notification.id.value.hashCode()
        val pendingIntent = PendingIntent.getActivity(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val built = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notification.message))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(id, built)
    }

    private fun hasPermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED

    companion object {
        const val CHANNEL_ID = "budget_alerts"
        const val EXTRA_CATEGORY_ID = "category_id"
    }
}
