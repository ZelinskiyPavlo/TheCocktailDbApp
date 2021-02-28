package com.test.cocktail_common.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*

const val ACTION_PROPOSE_DRINK = "com.test.thecocktaildb.action.PROPOSE_DRINK"

class DrinkProposalService : Service() {

    companion object {
        const val ACTION_START_SERVICE = "com.test.thecocktaildb.action.ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "com.test.thecocktaildb.action.ACTION_STOP_SERVICE"

        const val SELECTED_COCKTAIL_ID = "SELECTED_COCKTAIL_ID"
    }

    private var job: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val proposalIntent = Intent(ACTION_PROPOSE_DRINK).apply {
            putExtra(SELECTED_COCKTAIL_ID, intent?.getLongExtra(SELECTED_COCKTAIL_ID, -1L))
        }

        when (intent?.action) {
            ACTION_START_SERVICE -> {
                job = CoroutineScope(Dispatchers.IO).launch {
                    delay(3_000)
                    sendBroadcast(proposalIntent)
                }
            }
            ACTION_STOP_SERVICE -> job?.cancel()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        job?.cancel()
        job = null
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}