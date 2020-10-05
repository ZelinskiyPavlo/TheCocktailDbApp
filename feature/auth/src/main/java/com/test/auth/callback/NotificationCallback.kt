package com.test.auth.callback

import android.content.Intent

interface NotificationCallback {

    fun handleBroadcastNotification(intent: Intent)
}