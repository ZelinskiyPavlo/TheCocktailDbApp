package com.test.thecocktaildb.util.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.test.thecocktaildb.R
import com.test.thecocktaildb.core.common.firebase.Fcm
import com.test.thecocktaildb.presentation.ui.auth.AuthActivity
import com.test.thecocktaildb.util.receiver.ImplicitNotificationReceiver
import timber.log.Timber

class FcmService : FirebaseMessagingService(), LifecycleObserver {

    var isAppInForeground: Boolean = false

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            sendNotification(remoteMessage.data)
        }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        Timber.i("Fcm token updated $newToken")
    }

    private fun sendNotification(notificationProperties: Map<String, String>) {
        val notificationTitle = notificationProperties[Fcm.TITLE]
        val notificationBody = notificationProperties[Fcm.BODY]
        val notificationType = NotificationType.values()
            .find { it.key == notificationProperties[Fcm.TYPE] }?.ordinal.toString()
        val cocktailId = notificationProperties[Fcm.COCKTAIL_ID]
        val requestCode = notificationProperties[Fcm.ID]?.toInt() ?: 0

        val intent =
            if (isAppInForeground) Intent(this, ImplicitNotificationReceiver::class.java)
            else Intent(this, AuthActivity::class.java)
        intent.putExtra(Fcm.EXTRA_KEY_NOTIFICATION_TYPE, notificationType)
        intent.putExtra(Fcm.EXTRA_KEY_COCKTAIL_ID, cocktailId)

        val pendingIntent =
            if (isAppInForeground)
                PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)
            else PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder: NotificationCompat.Builder? = null
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                packageName,
                packageName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = packageName
            notificationManager.createNotificationChannel(channel)
            if (notificationBuilder == null) {
                notificationBuilder = NotificationCompat.Builder(application, packageName)
            }
        } else {
            if (notificationBuilder == null) {
                notificationBuilder = NotificationCompat.Builder(application, packageName)
            }
        }
        @Suppress("UsePropertyAccessSyntax")
        notificationBuilder
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notificationTitle ?: getString(R.string.app_name))
            .setContentText(notificationBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(1)

        notificationManager.notify(requestCode, notificationBuilder.build())
    }

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForegroundStart() {
        isAppInForeground = true
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onForegroundStop() {
        isAppInForeground = false
    }
}

enum class NotificationType(val key: String) {
    MAIN("main"),
    PROFILE("profile"),
    PROFILE_EDIT("profile_edit"),
    DETAIL("cocktail_detail"),
    RATE("rate_app")
}