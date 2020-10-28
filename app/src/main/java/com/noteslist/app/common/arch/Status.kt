package com.noteslist.app.common.arch

sealed class Status {
    object Loading : Status()
    object Success : Status()
    object Complete : Status()
    class Error(val e: Throwable) : Status()
}