package com.test.thecocktaildb.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.thecocktaildb.core.common.firebase.Fcm

class ImplicitNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            with(Intent(Fcm.ACTION_NEW_NOTIFICATION)) {
                putExtra(Fcm.EXTRA_KEY_NOTIFICATION_TYPE,
                    intent!!.getStringExtra(Fcm.EXTRA_KEY_NOTIFICATION_TYPE))
                putExtra(Fcm.EXTRA_KEY_COCKTAIL_ID,
                    intent.getStringExtra(Fcm.EXTRA_KEY_COCKTAIL_ID))
                context.sendBroadcast(this)
            }
        }
    }
}