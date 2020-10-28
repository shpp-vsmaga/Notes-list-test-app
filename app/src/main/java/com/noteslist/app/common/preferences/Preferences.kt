package com.noteslist.app.common.preferences

import devliving.online.securedpreferencestore.SecuredPreferenceStore

private const val PREF_KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL"

class Preferences(private val sharedPreferences: SecuredPreferenceStore) : PreferencesContract {

    override fun getUserEmail() =
        getString(PREF_KEY_USER_EMAIL, "")

    override fun setUserEmail(userToken: String) =
        putString(PREF_KEY_USER_EMAIL, userToken)

    override fun clear() {
        sharedPreferences.Editor().clear().apply()
    }

    private fun putString(key: String, value: String) =
        sharedPreferences.edit().putString(key, value).apply()

    private fun getString(key: String, defaultValue: String): String =
        sharedPreferences.getString(key, defaultValue) ?: defaultValue

}