package com.test.thecocktaildb.util.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler

    fun trampoline(): Scheduler

    fun computation(): Scheduler

}