package com.noteslist.app.common.preferences

interface PreferencesContract {

    fun clear()

    fun getUserEmail(): String
    fun setUserEmail(userToken: String)
}