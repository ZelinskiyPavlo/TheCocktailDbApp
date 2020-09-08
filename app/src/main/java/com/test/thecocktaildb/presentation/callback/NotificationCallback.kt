package com.test.thecocktaildb.presentation.callback

import android.content.Intent

interface NotificationCallback {

    fun handleBroadcastNotification(intent: Intent)
}