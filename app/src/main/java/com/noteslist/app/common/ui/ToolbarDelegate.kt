package com.noteslist.app.common.ui

import android.view.Menu
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.noteslist.app.R
import timber.log.Timber

class ToolbarDelegate(private val fragment: Fragment) {

    private var toolbar: Toolbar = fragment.view?.findViewById(R.id.toolbar) as? Toolbar
        ?: throw IllegalStateException("Fragment should have toolbar view with 'R.id.toolbar' id")

    var toolbarNavigateUpListener: ToolbarNavigateUpListener? = null

    fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            navigateUp()
        }
    }

    fun navigateUp() {
        val onNavigateUp = toolbarNavigateUpListener?.onNavigateUp() ?: false
        if (!onNavigateUp) {
            val handled = getNavController()?.popBackStack() ?: false
            if (!handled) {
                fragment.activity?.onBackPressed()
            }
        }
    }


    private fun getNavController(): NavController? {
        return try {
            fragment.view?.let { Navigation.findNavController(it) }
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }

    fun setToolbarTitle(@StringRes titleRes: Int) {
        toolbar.setTitle(titleRes)
    }

    fun setToolbarSubtitle(@StringRes titleRes: Int) {
        toolbar.setSubtitle(titleRes)
    }

    fun setToolbarTitle(title: String) {
        toolbar.setTitle(title)
    }

    fun inflateMenu(@MenuRes menuRes: Int): Menu? {
        toolbar.inflateMenu(menuRes)
        return toolbar.menu
    }

    fun clearMenu() = toolbar.menu?.clear()
}

interface ToolbarNavigateUpListener {
    fun onNavigateUp(): Boolean
}