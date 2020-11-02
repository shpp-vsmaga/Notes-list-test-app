package com.noteslist.app.screens.main.ui

import com.noteslist.app.common.arch.BaseVM
import com.noteslist.app.common.arch.ViewStateLiveData
import com.noteslist.app.common.livedata.SingleLiveEvent

interface MainActivityVM: BaseVM {
    val isAuthorizedAction: SingleLiveEvent<Boolean>
}