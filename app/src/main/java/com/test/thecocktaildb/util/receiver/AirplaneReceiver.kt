package com.test.thecocktaildb.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import timber.log.Timber

class AirplaneReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, airplaneModeIntent: Intent?) {
        Timber.i("onReceive method of AirplaneReceiver")
        val airplaneModeStatus = airplaneModeIntent?.getBooleanExtra("state", false) ?: false
        if (airplaneModeStatus) {
            val message = "Смакуйте маргаритку із відчуттям міри. І надіємося, що Ви не пілот під" +
                    "  час польоту"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}