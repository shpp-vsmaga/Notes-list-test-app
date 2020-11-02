package com.noteslist.app.screens.main.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputLayout
import com.noteslist.app.BR
import com.noteslist.app.R
import com.noteslist.app.common.di.viewModel
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
        Log.d("svcom", "saved instance on create - ${savedInstanceState}")
//        if (savedInstanceState == null) {
        initActivity()
//        }
    }

    private fun initActivity() {
        viewModel.isAuthorizedAction.observe(owner = this,
            { handleStartNavigation(it) }
        )
    }

    private fun handleStartNavigation(isAuthorized: Boolean) {
        if (isAuthorized) {
            navigateToStartDestination(R.id.notesScreenFragment)
        } else {
            navigateToStartDestination(R.id.authScreenFragment)
        }
    }

    private fun navigateToStartDestination(@IdRes id: Int) {
        val navHostFragment = navHostFragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.app_graph)
        graph.startDestination = id
        navHostFragment.navController.graph = graph
    }

    @Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText || v is TextInputLayout) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}