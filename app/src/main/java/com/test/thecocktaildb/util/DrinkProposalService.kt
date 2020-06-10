package com.test.thecocktaildb.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DrinkProposalService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        for (i in 1..5) {
            try {
                Toast.makeText(applicationContext, "$i", Toast.LENGTH_SHORT).show()
                Timber.i("onStartCommand $i")
                TimeUnit.SECONDS.sleep(3)
            } catch (e: Exception) {

            }
        }
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