package com.test.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.test.firebase.common.Fcm

/** I'm not sure if this is correct place to create notification. On the one hand FCM is about
 * showing notification, but on the other hand firebase is concrete platform implementation and
 * notification creating is not depend on firebase.
 * */
class NotificationCreator(
    private val context: Context,
    private val intent: Intent,
    notificationData: Map<String, String>
) {

    private var title = notificationData[Fcm.TITLE]
    private var body = notificationData[Fcm.BODY]

    private var type = notificationData[Fcm.TYPE]
    private var cocktailId = notificationData[Fcm.COCKTAIL_ID]

    private var requestCode = notificationData[Fcm.ID]?.toInt() ?: 0

    private lateinit var pendingIntent: PendingIntent

    private var notificationBuilder = NotificationCompat.Builder(context, context.packageName)
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val appName = context.getString(R.string.app_name)

    fun showNotification() {
        prepareIntent()
        addNotificationChannel()
        buildNotification()

        notificationManager.notify(requestCode, notificationBuilder.build())
    }

    private fun prepareIntent() {
        intent.putExtra(Fcm.EXTRA_KEY_NOTIFICATION_TYPE, type)
        intent.putExtra(Fcm.EXTRA_KEY_COCKTAIL_ID, cocktailId)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        pendingIntent = PendingIntent.getActivity(
            context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun addNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                context.packageName,
                appName.plus(" channel"),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification() {
        notificationBuilder
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title ?: appName)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(1)
            .setContentIntent(pendingIntent)
    }
}