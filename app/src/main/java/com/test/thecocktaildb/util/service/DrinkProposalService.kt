package com.test.thecocktaildb.util.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

const val ACTION_PROPOSE_DRINK = "com.test.thecocktaildb.action.PROPOSE_DRINK"

class DrinkProposalService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val proposalIntent = Intent(ACTION_PROPOSE_DRINK)
        proposalIntent.putExtra(Intent.EXTRA_TEXT, intent?.getStringExtra(Intent.EXTRA_TEXT))

        Observable.timer(3, TimeUnit.SECONDS)
            .map { sendBroadcast(proposalIntent) }.subscribe()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}