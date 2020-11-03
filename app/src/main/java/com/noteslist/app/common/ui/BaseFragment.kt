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
import com.noteslist.app.common.arch.BaseViewModel
import com.noteslist.app.common.utils.changeStatusBarColor
import com.noteslist.app.common.utils.showMessage
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import timber.log.Timber

/**
 * Base implementation of Fragment in MVVM, that use DataBinding
 * Use to avoid a lot of boilerplate code when creating new screen,
 * We simply need to set types of ViewModel and DataBinding,
 * then pass viewModel instance and layout resource id to the corresponding fields
 */
abstract class BaseFragment<V: BaseViewModel, B : ViewDataBinding> :
    Fragment(),
    KodeinAware {
    override val kodein: Kodein by closestKodein()

    private lateinit var binding: B

    protected abstract val viewModel: V

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
        init()
        initFragment()
        setupStatusBar()
    }

    private fun setupStatusBar() {
        val isLight = true
        val color = R.color.color_white
        changeStatusBarColor(
            activity,
            resources.getColor(color, activity?.theme),
            lightStatusBar = isLight
        )
    }

    private fun init() {
        viewModel.errorLiveData.observe(viewLifecycleOwner, createErrorObserver())
    }

    private fun createErrorObserver(): Observer<String> {
        return Observer {
            showMessage(it)
        }
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