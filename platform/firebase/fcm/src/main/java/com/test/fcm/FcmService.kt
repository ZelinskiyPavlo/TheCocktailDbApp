package com.test.fcm

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.test.dagger.DiConstant
import com.test.repository.source.TokenRepository
import dagger.android.AndroidInjection
import javax.inject.Inject
import javax.inject.Named

class FcmService: FirebaseMessagingService() {

    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    @field:Named(DiConstant.ACTIVITY_INTENT)
    lateinit var intent: Intent

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        tokenRepository.firebaseToken = token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            NotificationCreator(this, intent, remoteMessage.data).showNotification()
        }
    }
}