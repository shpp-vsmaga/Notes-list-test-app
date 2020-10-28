package com.noteslist.app.common.arch

import androidx.lifecycle.LiveData

interface BaseVM {
    val progressVisible: LiveData<Boolean>
    val errorLiveData: LiveData<String>
}