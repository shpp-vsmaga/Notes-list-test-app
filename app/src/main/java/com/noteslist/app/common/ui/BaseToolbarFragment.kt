package com.noteslist.app.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.noteslist.app.BR
import com.noteslist.app.R
import com.noteslist.app.common.arch.BaseVM
import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.common.utils.changeStatusBarColor
import com.noteslist.app.common.utils.showMessage
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import timber.log.Timber

abstract class BaseToolbarFragment<V : BaseViewModel, B : ViewDataBinding> :
    Fragment(),
    KodeinAware {
    override val kodein: Kodein by closestKodein()

    protected lateinit var binding: B

    protected abstract val viewModel: V

    var toolbarDelegate: ToolbarDelegate? = null

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected abstract fun initFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                layoutResId(),
                container,
                false
            )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.executePendingBindings()
        setupToolbar()
        setupStatusBar()
        init()
        initFragment()
    }

    private fun init() {
        viewModel.errorLiveData.observe(viewLifecycleOwner, createErrorObserver())
    }

    private fun createErrorObserver(): Observer<String> {
        return Observer {
            showMessage(it)
        }
    }

    private fun setupStatusBar() {
        val isLight = true
        val color = R.color.color_white
        changeStatusBarColor(
            activity,
            resources.getColor(color),
            lightStatusBar = isLight
        )
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

    fun getNavController(): NavController? {
        return try {
            view?.let { Navigation.findNavController(it) }
        } catch (e: Throwable) {
            Timber.e(e)
            null
        }
    }
}