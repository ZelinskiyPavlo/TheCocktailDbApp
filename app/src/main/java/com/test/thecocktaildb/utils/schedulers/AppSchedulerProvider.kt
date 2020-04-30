package com.test.thecocktaildb.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AppSchedulerProvider @Inject constructor() : SchedulerProvider {

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun trampoline(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.computation()
}