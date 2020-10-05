package com.test.setting.callback

import com.test.thecocktaildb.util.batterystate.BatteryStateHolder

interface BatteryStateCallback {

    fun updateBatteryState(batteryState: BatteryStateHolder)
}