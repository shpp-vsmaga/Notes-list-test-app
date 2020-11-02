package com.noteslist.app.common.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class  BaseVM: ViewModel() {
    abstract val progressVisible: LiveData<Boolean>
    abstract val errorLiveData: LiveData<String>
}