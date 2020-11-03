package com.noteslist.app.auth.useCases

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Single

class AuthUseCases(private val context: Context, private val firebaseAuth: FirebaseAuth) {

    /**
     * Checks is user authorized in Firebase Auth
     * @return RX Single with boolean value, true - user is authorized, false - not
     */
    fun checkIsAuthorized(): Single<Boolean> =
        Single.just(firebaseAuth.currentUser != null)


    /**
     * Performs logout actions in Firebase Auth
     * @return RX Completable that signals about operation complete or error
     */
    fun logout(): Completable =
        Completable.create { emitter ->
            AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> emitter.onComplete()
                        else -> emitter.onError(it.exception ?: Exception("User canceled"))
                    }
                }

        }
}