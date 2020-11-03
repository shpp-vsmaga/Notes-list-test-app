package com.noteslist.app.screens.main.ui

import com.noteslist.app.auth.useCases.AuthUseCases
import com.noteslist.app.common.livedata.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivityVMImpl(private val authUseCases: AuthUseCases) : MainActivityVM() {

    override val isAuthorizedAction = SingleLiveEvent<Boolean>()

    init {
        checkIsAuthorized()
    }

    private fun checkIsAuthorized() {
        authUseCases.checkIsAuthorized()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isAuthorizedAction.value = it
            }, {
                showError(it)
            })
            .disposeOnCleared()


    }
}