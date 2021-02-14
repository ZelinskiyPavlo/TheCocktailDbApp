package com.test.setting.callback

import com.test.setting.model.BatteryStateHolder

interface BatteryStateCallback {

    fun updateBatteryState(batteryState: BatteryStateHolder)
}