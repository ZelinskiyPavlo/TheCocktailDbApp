package com.test.cocktail.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.thecocktaildb.util.service.OnBootService

class OnBootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED){
            val newIntent = Intent(context, OnBootService::class.java)
            newIntent.action = Intent.ACTION_BOOT_COMPLETED
            context?.startService(newIntent)
        }
    }
}