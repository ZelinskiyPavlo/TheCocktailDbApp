package com.test.thecocktaildb.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class DrinkProposalReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("onReceive after 3 seconds of sleep")

//        TODO: intent name may be changed
        val selectedCocktailId = intent?.getIntExtra("SelectedDrink", -1)

    }
}