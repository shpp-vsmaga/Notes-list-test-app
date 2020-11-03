package com.noteslist.app.common.arch


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noteslist.app.common.livedata.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    private val _progressVisible = MutableLiveData(false)
    val progressVisible: LiveData<Boolean>
        get() = _progressVisible

    private val _errorLiveData = SingleLiveEvent<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

    private val compositeDisposable = CompositeDisposable()

    /**
     * Add current Disposable to composite disposable;
     * All added Disposables will be disposed when ViewModel call onCleared method,
     * e.g. when we leaved from Fragment or Activity
     */
    protected fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }

    /**
     * It will be called by ViewModel when we leaved from Fragment or Activity
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun showProgress() {
        _progressVisible.postValue(true)
    }

    protected fun hideProgress() {
        _progressVisible.postValue(false)
    }

    protected fun showError(error: String?) {
        _errorLiveData.postValue(error)
    }

    protected fun showError(error: Throwable?) {
        _errorLiveData.postValue(error?.message)
    }
}