package com.test.setting.callback

import com.test.setting.model.BatteryStateHolder

internal interface BatteryStateCallback {

    fun updateBatteryState(batteryState: BatteryStateHolder)
}