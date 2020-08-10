package com.test.thecocktaildb.ui.cocktail.callback

import com.test.thecocktaildb.util.batterystate.BatteryStateHolder

interface BatteryStateCallback {

    fun updateBatteryState(batteryState: BatteryStateHolder)
}