package com.noteslist.app.common.arch

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class ViewStateLiveData<T>(
    private val isDataOneShot: Boolean = false,
    private val isLoadingOneShot: Boolean = false,
    private val isErrorOneShot: Boolean = false,
    private val isCompleteOneShot: Boolean = false
) : MutableLiveData<ViewState<T>>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in ViewState<T>>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { viewState ->
            if (!isStateOneShot(viewState) || pending.compareAndSet(true, false)) {
                observer.onChanged(viewState)
            }
        })
    }

    @MainThread
    override fun setValue(t: ViewState<T>?) {
        pending.set(true)
        super.setValue(t)
    }

    fun onNext(data: T) {
        value = ViewState.success(data)
    }

    fun onError(e: Throwable) {
        value = ViewState.error(e, value?.data)
    }

    fun onComplete() {
        value = ViewState.complete(value?.data)
    }

    fun onStart() {
        value = ViewState.loading(value?.data)
    }

    private fun isStateOneShot(state: ViewState<T>) =
        when (state.status) {
            Status.Loading -> isLoadingOneShot
            Status.Success -> isDataOneShot
            Status.Complete -> isCompleteOneShot
            is Status.Error -> isErrorOneShot
        }

    companion object {
        fun <T> create() = ViewStateLiveData<T>()

        fun <T> createForSingle() = ViewStateLiveData<T>(
            isDataOneShot = true,
            isCompleteOneShot = true,
            isErrorOneShot = true
        )
    }
}