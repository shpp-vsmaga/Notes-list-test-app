package com.noteslist.app.common.di

import android.content.Context
import android.util.Base64
import com.noteslist.app.common.preferences.Preferences
import com.noteslist.app.common.preferences.PreferencesContract
import devliving.online.securedpreferencestore.DefaultRecoveryHandler
import devliving.online.securedpreferencestore.SecuredPreferenceStore
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.security.SecureRandom

object PreferenceInjectionModule : InjectionModule {

    private const val SECURED_PREFERENCE_FILE_NAME = "sprefs"
    private const val UNSECURED_PREFERENCE_FILE_NAME = "prefs"

    // this is obfuscated on purpose
    private const val A = "A"
    private const val B = 40

    override val module = Kodein.Module(PreferenceInjectionModule::class.java.name) {

        bind<SecuredPreferenceStore>() with singleton {

            val context: Context = instance()

            val pref = context.getSharedPreferences(UNSECURED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
            var keyBase64: String? = pref.getString(A, null)
            if (keyBase64 == null) {
                keyBase64 = Base64.encodeToString(SecureRandom.getSeed(B), Base64.DEFAULT)
                pref.edit().putString(A, keyBase64).apply()
            }

            SecuredPreferenceStore.init(
                context,
                SECURED_PREFERENCE_FILE_NAME,
                null,
                Base64.decode(keyBase64, Base64.DEFAULT),
                DefaultRecoveryHandler()
            )
            return@singleton SecuredPreferenceStore.getSharedInstance()
        }

        bind<PreferencesContract>() with singleton {
            Preferences(instance())
        }
    }
}