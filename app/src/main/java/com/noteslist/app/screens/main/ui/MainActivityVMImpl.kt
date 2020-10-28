package com.noteslist.app.screens.main.ui

import com.noteslist.app.auth.useCases.AuthUseCases
import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.common.arch.ViewStateLiveData
import com.noteslist.app.common.arch.postToViewStateLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivityVMImpl(private val authUseCases: AuthUseCases) : BaseViewModel(), MainActivityVM {

    override val isAuthorizedState = ViewStateLiveData<Boolean>()

    init {
        checkIsAuthorized()
    }

    private fun checkIsAuthorized() {
        authUseCases.checkIsAuthorized()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .postToViewStateLiveData(isAuthorizedState)
            .disposeOnCleared()


    }
}