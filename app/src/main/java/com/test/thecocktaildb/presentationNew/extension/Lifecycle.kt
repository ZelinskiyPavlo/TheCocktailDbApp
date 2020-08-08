package com.test.thecocktaildb.presentationNew.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

inline fun Lifecycle.addOnCreateObserver(crossinline onCreate: LifecycleObserver.() -> Unit) {
    addObserver(
        object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = onCreate.invoke(this)
        }
    )
}

inline fun Lifecycle.doOnCreate(crossinline onCreate: () -> Unit) {
    if (currentState.isAtLeast(Lifecycle.State.CREATED)) {
        onCreate()
    } else {
        addObserver(
            object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                fun onCreate() {
                    onCreate.invoke()
                    removeObserver(this)
                }
            }
        )
    }
}

inline fun Lifecycle.addObserver(
    crossinline onCreate: LifecycleObserver.() -> Unit,
    crossinline onDestroy: LifecycleObserver.() -> Unit
) {
    addObserver(
        object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = onCreate.invoke(this)

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() = onDestroy.invoke(this)
        }
    )
}