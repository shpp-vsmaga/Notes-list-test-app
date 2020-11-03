package com.noteslist.app.screens.main.ui

import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.common.livedata.SingleLiveEvent

abstract class MainActivityVM : BaseViewModel() {
    abstract val isAuthorizedAction: SingleLiveEvent<Boolean>
}