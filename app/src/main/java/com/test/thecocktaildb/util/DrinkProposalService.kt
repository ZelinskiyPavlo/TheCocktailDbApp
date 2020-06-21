package com.test.thecocktaildb.util

import android.app.Service
import android.content.Intent
import android.os.IBinder

class DrinkProposalService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}