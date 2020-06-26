package com.test.thecocktaildb.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.thecocktaildb.ui.cocktailsScreen.callback.DrinkProposalCallback

class DrinkProposalReceiver(
    private val drinkProposalCallback: DrinkProposalCallback
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val selectedCocktailId = intent?.getStringExtra(Intent.EXTRA_TEXT) ?: ""

        drinkProposalCallback.proposeCocktail(selectedCocktailId)
    }
}