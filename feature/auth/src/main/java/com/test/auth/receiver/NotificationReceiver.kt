package com.test.auth.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.auth.callback.NotificationCallback

class NotificationReceiver(
    private val notificationCallback: NotificationCallback
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { notificationCallback.handleBroadcastNotification(intent) }
    }
}