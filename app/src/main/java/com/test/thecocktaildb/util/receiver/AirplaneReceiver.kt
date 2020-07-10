package com.test.thecocktaildb.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.test.thecocktaildb.R
import timber.log.Timber

class AirplaneReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, airplaneModeIntent: Intent?) {
        Timber.i("onReceive method of AirplaneReceiver")
        val airplaneModeStatus = airplaneModeIntent?.getBooleanExtra("state", false) ?: false
        if (airplaneModeStatus) {
            val message = context?.getString(R.string.broadcast_receiver_airplane_status_toast)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}