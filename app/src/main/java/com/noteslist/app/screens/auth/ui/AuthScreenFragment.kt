package com.noteslist.app.screens.auth.ui

import android.app.Activity
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.noteslist.app.R
import com.noteslist.app.common.di.viewModel
import com.noteslist.app.common.ui.BaseFragment
import com.noteslist.app.common.utils.navigateTo
import com.noteslist.app.databinding.FragmentAuthBinding

class AuthScreenFragment : BaseFragment<AuthScreenVM, FragmentAuthBinding>() {
    override val viewModel: AuthScreenVM by viewModel()

    override fun layoutResId() = R.layout.fragment_auth

    override fun initFragment() {
        viewModel.signInEvent.observe(viewLifecycleOwner, {
            handleSignInEvent(it)
        })
    }

    private fun handleSignInEvent(event: AuthScreenVM.Companion.SignInEvent?) {
        when (event) {
            AuthScreenVM.Companion.SignInEvent.OPEN_SIGN_IN -> openSignInFlow()
            AuthScreenVM.Companion.SignInEvent.SIGN_IN_SUCCESS -> openNotesScreen()
        }
    }

    private fun openSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
                .build(),
            RC_SIGN_IN
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    viewModel.signInSuccess(user.uid)
                }
            } else {
                viewModel.signInError(response?.error?.message)
            }
        }
    }

    private fun openNotesScreen() {
        getNavController()?.navigateTo(R.id.action_authScreenFragment_to_notesScreenFragment)
    }


    companion object {
        const val RC_SIGN_IN = 100
    }
}