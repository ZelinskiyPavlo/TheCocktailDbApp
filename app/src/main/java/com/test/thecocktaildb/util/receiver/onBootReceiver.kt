package com.test.thecocktaildb.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.thecocktaildb.util.service.OnBootService
import timber.log.Timber

class OnBootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED){
            Timber.i("___________onReceive")
            val newIntent = Intent(context, OnBootService::class.java)
            newIntent.action = Intent.ACTION_BOOT_COMPLETED
            context?.startService(newIntent)
        }
    }
}