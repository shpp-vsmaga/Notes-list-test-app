package com.noteslist.app.common.arch


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteslist.app.BuildConfig
import com.noteslist.app.common.livedata.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {
    private val _progressVisible = MutableLiveData(false)
    val progressVisible: LiveData<Boolean>
        get() = _progressVisible

    private val _errorLiveData = SingleLiveEvent<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData


    private val exceptionHandler: CoroutineContext =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            handleException(coroutineContext, throwable)
        }

    protected val scope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

    open fun handleException(coroutineContext: CoroutineContext, error: Throwable) {
        if (BuildConfig.DEBUG) error.printStackTrace()
        showError(error)
        hideProgress()
    }

    protected fun runCoroutine(
        exceptionHandler: ((Exception) -> Boolean)? = null,
        withProgress: Boolean = false,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch {
            if (withProgress) showProgress()
            try {
                block.invoke(this)
            } catch (e: Exception) {
                if (exceptionHandler?.invoke(e) != false)
                    handleException(coroutineContext, e)
            }
            if (withProgress) hideProgress()
        }
    }


    private fun showProgress() {
        _progressVisible.postValue(true)
    }

    private fun hideProgress() {
        _progressVisible.postValue(false)
    }

    protected fun showError(error: String?) {
        _errorLiveData.postValue(error)
    }

    protected fun showError(error: Throwable?) {
        _errorLiveData.postValue(error?.message)
    }
}