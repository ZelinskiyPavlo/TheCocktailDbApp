package com.test.thecocktaildb.util.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DrinkProposalService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("onStartCommand of Service")
        val proposalIntent = Intent("TEST_ACTION")

        Observable.timer(3, TimeUnit.SECONDS)
            .map { sendBroadcast(proposalIntent) }.subscribe()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate service")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy service")
    }
}