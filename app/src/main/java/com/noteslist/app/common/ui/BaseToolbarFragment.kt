package com.noteslist.app.common.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.noteslist.app.common.arch.BaseViewModel
import org.kodein.di.KodeinAware

/**
 * @see BaseFragment with added functionality to manage toolbar
 */
abstract class BaseToolbarFragment<V : BaseViewModel, B : ViewDataBinding> :
    BaseFragment<V, B>(),
    KodeinAware {

    var toolbarDelegate: ToolbarDelegate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbarDelegate = ToolbarDelegate(this)
        toolbarDelegate?.setupToolbar()
    }

    fun setToolbarTitle(title: String) {
        toolbarDelegate?.setToolbarTitle(title)
    }

    fun setToolbarTitle(titleResId: Int) {
        toolbarDelegate?.setToolbarTitle(titleResId)
    }
}