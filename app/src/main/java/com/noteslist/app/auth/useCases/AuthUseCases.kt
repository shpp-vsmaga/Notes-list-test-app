package com.noteslist.app.auth.useCases

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthUseCases(private val context: Context, private val firebaseAuth: FirebaseAuth) {

    /**
     * Checks is user authorized in Firebase Auth
     * @return boolean value, true - user is authorized, false - not
     */
    fun checkIsAuthorized() =
        firebaseAuth.currentUser != null


    /**
     * Performs logout actions in Firebase Auth
     */
    suspend fun logout() {
        suspendCancellableCoroutine<Unit> { continuation ->
            AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener {
                    when {
                        it.isSuccessful -> continuation.resume(Unit)
                        else -> continuation.resumeWithException(
                            it.exception ?: Exception("User canceled")
                        )
                    }


                }
        }
    }
}