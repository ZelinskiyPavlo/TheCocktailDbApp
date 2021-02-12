package com.test.cocktail_common.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

const val ACTION_PROPOSE_DRINK = "com.test.thecocktaildb.action.PROPOSE_DRINK"

// TODO: 07.01.2021 я думаю, тут цього не має бути. Треба через DI вирішувати додавання певних класів
class DrinkProposalService : Service() {

    private var serviceStopFlag: Boolean = false
//    private var timerObservable: Disposable? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val proposalIntent = Intent(ACTION_PROPOSE_DRINK)
        proposalIntent.putExtra(
            SELECTED_COCKTAIL_ID, intent?.getLongExtra(SELECTED_COCKTAIL_ID, -1L)
        )

        when (intent?.action) {
            ACTION_STOP_SERVICE -> serviceStopFlag = false
            ACTION_START_SERVICE -> serviceStopFlag = true
        }


//        if (serviceStopFlag) {
//            timerObservable = Observable
//                .timer(3, TimeUnit.SECONDS)
//                .map {
//                    if (serviceStopFlag) sendBroadcast(proposalIntent)
//                }
//                .subscribe()
//        } else {
//            if (timerObservable?.isDisposed?.not() == true) {
//                timerObservable?.dispose()
//                stopForeground(true)
//                stopSelf()
//            }
//        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
//        if (timerObservable?.isDisposed?.not() == true) timerObservable?.dispose()
        super.onDestroy()
    }

    companion object {
        const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

        const val SELECTED_COCKTAIL_ID = "SELECTED_COCKTAIL_ID"
    }
}