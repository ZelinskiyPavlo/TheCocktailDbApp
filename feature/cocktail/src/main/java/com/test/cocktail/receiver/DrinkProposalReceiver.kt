package com.test.cocktail.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.cocktail.receiver.callback.DrinkProposalCallback
import com.test.cocktail_common.service.DrinkProposalService

internal class DrinkProposalReceiver(
    private val drinkProposalCallback: DrinkProposalCallback
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val selectedCocktailId =
            intent?.getLongExtra(DrinkProposalService.SELECTED_COCKTAIL_ID, -1L) ?: -1L

        drinkProposalCallback.proposeCocktail(selectedCocktailId)
    }
}