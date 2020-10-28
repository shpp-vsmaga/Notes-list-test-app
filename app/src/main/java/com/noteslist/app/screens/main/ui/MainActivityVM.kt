package com.noteslist.app.screens.main.ui

import com.noteslist.app.common.arch.BaseVM
import com.noteslist.app.common.arch.ViewStateLiveData

interface MainActivityVM: BaseVM {
    val isAuthorizedState: ViewStateLiveData<Boolean>
}