package com.noteslist.app.screens.main.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.noteslist.app.BR
import com.noteslist.app.R
import com.noteslist.app.common.arch.observeViewState
import com.noteslist.app.common.di.viewModel
import com.noteslist.app.common.utils.navigateToUnsafe
import com.noteslist.app.common.utils.showMessage
import com.noteslist.app.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by closestKodein()

    private val viewModel: MainActivityVMImpl by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.setVariable(BR.vm, viewModel)
        initActivity()
    }

    private fun initActivity() {
        viewModel.isAuthorizedState.observeViewState(
            owner = this,
            dataCallback = ::handleStartNavigation,
            errorCallback = ::handleError
        )
    }

    private fun handleStartNavigation(isAuthorized: Boolean) {
        if (isAuthorized) {
            navigateToStartDestination(R.id.notesScreenFragment)
        } else {
            navigateToStartDestination(R.id.authScreenFragment)
        }
    }

    private fun handleError(error: Throwable) {
        error.message?.let {
            showMessage(it)
        }
    }

    private fun navigateToStartDestination(@IdRes id: Int, bundle: Bundle? = null) {
        val navHostFragment = navHostFragment as NavHostFragment
        navHostFragment.navController.popBackStack(R.id.splashScreenFragment, true)
        navHostFragment.navController.navigateToUnsafe(
            id,
            bundle,
            navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.navHostFragment, true)
                .build()
        )
    }
}