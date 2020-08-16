package com.test.thecocktaildb.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.thecocktaildb.presentation.callback.NotificationCallback

class NotificationReceiver(
    private val notificationCallback: NotificationCallback
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { notificationCallback.handleBroadcastNotification(intent) }
    }
}