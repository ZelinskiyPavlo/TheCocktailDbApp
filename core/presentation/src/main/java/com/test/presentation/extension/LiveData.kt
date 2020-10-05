package com.test.presentation.extension

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.*

@MainThread
inline fun <reified T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner, Observer { observer(it) })
}

@MainThread
inline fun <T> LiveData<T?>.observeNotNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner, Observer {
        if (it != null) observer(it)
    })
}

@MainThread
inline fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observer(t)
        }
    })
}

@MainThread
inline fun <T> LiveData<T>.observeOnce(crossinline observer: (T) -> Unit) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observer(t)
        }
    })
}

@MainThread
inline fun <T> LiveData<T?>.observeNotNullOnce(crossinline observer: (T) -> Unit) {
    observeForever(object : Observer<T?> {
        override fun onChanged(t: T?) {
            if (t != null) {
                removeObserver(this)
                observer(t)
            }
        }
    })
}

@MainThread
inline fun <T> LiveData<T>.observeUntil(
    lifecycleOwner: LifecycleOwner? = null,
    crossinline predicate: (T) -> Boolean,
    crossinline observer: (T) -> Unit
): Observer<T> {
    val observerObject = object : Observer<T> {
        override fun onChanged(t: T) {
            if (predicate(t)) {
                removeObserver(this)
                observer(t)
            }
        }
    }
    if (lifecycleOwner == null) observeForever(observerObject)
    else observe(lifecycleOwner, observerObject)
    return observerObject
}

@MainThread
inline fun <reified T, reified R : Collection<T>> LiveData<R>.observeNotEmptyOnce(
    crossinline observer: (R) -> Unit
): Observer<R> {
    val observerObject = object : Observer<R> {
        override fun onChanged(t: R) {
            if (t.isNotEmpty()) {
                removeObserver(this)
                observer(t)
            }
        }
    }
    observeForever(observerObject)
    return observerObject
}

@MainThread
inline fun <T> LiveData<T>.observeNotNullOnce(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            if (t != null) {
                removeObserver(this)
                observer(t)
            }
        }
    })
}

inline fun <reified T> LiveData<T?>.mapNotNull(): LiveData<T> = MediatorLiveData<T>().also {
    it.addSource(this) { newValue -> if (newValue != null) it.value = newValue }
}

inline fun <reified T, reified R> LiveData<T?>.mapNotNull(crossinline predicate: T.() -> R = { this as R }): LiveData<R> =
    MediatorLiveData<R>().also {
        it.addSource(this) { newValue -> if (newValue != null) it.value = newValue.predicate() }
    }

//inline fun <reified T, reified R> LiveData<T>.mapNotNull(crossinline predicate: T.() -> R = { this as R }): LiveData<R> =
//    MediatorLiveData<R>().also {
//        it.addSource(this) { newValue -> if (newValue != null) it.value = newValue.predicate() }
//    }

fun <T> LiveData<T>.mapWithPrevious(
    initialValue: T,
    predicate: (current: T, new: T) -> T
): LiveData<T> =
    MediatorLiveData<T>().also {
        it.value = predicate(it.value ?: initialValue, this.value ?: initialValue)
        it.addSource(this) { newValue ->
            it.value = predicate(it.value!!, newValue)
        }
    }

fun <T> LiveData<T>.mapWithPrevious(predicate: (current: T?, new: T?) -> T?): LiveData<T?> =
    MediatorLiveData<T?>().also {
        it.value = predicate(it.value, this.value)
        it.addSource(this) { newValue ->
            it.value = predicate(it.value, newValue)
        }
    }

fun <T> LiveData<T>.distinctNotNullValues(onDistinct: (current: T, new: T) -> Unit = { _, _ -> }): LiveData<T?> =
    object : MediatorLiveData<T?>() {
        private var currentDistinctValue: T? = null

        init {
            this@distinctNotNullValues.value?.apply { currentDistinctValue = this }
            addSource(this@distinctNotNullValues) { newValue ->
                currentDistinctValue.let { distinctValue ->
                    if (distinctValue == null && newValue != null) {
                        currentDistinctValue = newValue
                    }
                    if (distinctValue != null && newValue != null && distinctValue != newValue) {
                        value = newValue
                        onDistinct(distinctValue, newValue)
                        currentDistinctValue = newValue
                    }
                }
            }
        }
    }

@MainThread
inline fun <T> LiveData<T?>.observeTillDestroyNotNull(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) return

    val liveDataObserver = Observer<T?> {
        if (it != null && owner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED))
            observer(it)
    }

    owner.lifecycle.addObserver(
        onCreate = { observeForever(liveDataObserver) },
        onDestroy = { removeObserver(liveDataObserver) }
    )
}

@MainThread
inline fun <T> LiveData<T>.observeTillDestroy(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) return

    val liveDataObserver = Observer<T?> {
        if (it != null && owner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED))
            observer(it)
    }

    owner.lifecycle.addObserver(
        onCreate = { observeForever(liveDataObserver) },
        onDestroy = { removeObserver(liveDataObserver) }
    )
}


fun <T> LiveData<T>.debounce(duration: Long = 300L): LiveData<T> = MediatorLiveData<T>().also {
    val source = this
    val handler = Handler(Looper.getMainLooper())

    val runnable = Runnable {
        it.value = source.value
    }

    it.addSource(source) {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, duration)
    }
}

fun <T> LiveData<T>.throttle(duration: Long = 300L): LiveData<T> =
    MediatorLiveData<T>().also { mld ->

        val source = this
        val throttle = Throttle(duration)

        mld.addSource(source) {
            throttle.attempt(Runnable { mld.value = it })
        }
    }

fun <T> mediatorLiveData(
    block: MediatorLiveData<T>.() -> Unit
): LiveData<T> = MediatorLiveData<T>().apply {
    block()
}

inline fun <T> dependantLiveData(
    defaultValue: T? = null,
    distinctUntilChanged: Boolean = true,
    vararg sources: LiveData<out Any?>,
    crossinline mapper: T?.() -> T?
): LiveData<T> = MediatorLiveData<T>()
    .also { mediatorLiveData ->
        val observer = if (distinctUntilChanged) {
            Observer<Any> {
                val newValue = mediatorLiveData.value.mapper()
                if (mediatorLiveData.value != newValue) mediatorLiveData.value = newValue
            }
        } else {
            Observer<Any> { mediatorLiveData.value = mediatorLiveData.value.mapper() }
        }
        sources.forEach { source ->
            mediatorLiveData.addSource(source, observer)
        }
    }.apply { value = defaultValue }

private class Throttle(private val interval: Long = 16L) {
    private var lastFiredTimestamp = -1L

    fun attempt(runnable: Runnable) {
        if (triggerRunnable) {
            runnable.run()
            lastFiredTimestamp = now
        }
    }

    private val triggerRunnable get() = (now - lastFiredTimestamp) >= interval
    private val now get() = System.currentTimeMillis()
}
