package com.noteslist.app.common.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noteslist.app.common.utils.threadUnsafeLazy
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance


/**
 * Example usage:
 *
 * Define your kodein binding as PROVIDER (since there is no argument):
 * ```kotlin
 *     bind<SomeViewModel>() with provider {
 *         SomeViewModel(instance(),... )
 *     }
 * ```
 * And then inject your view model in following manner:
 * ```kotlin
 *     class SomeFragment: Fragment(), KodeinAware {
 *         val viewModel: SomeViewModel by viewModel()
 *     }
 * ```
 * It is IMPORTANT to define view model variable type, otherwise you have to define all methods generics - not so cool.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified TViewModel, TFragment> TFragment.viewModel(
    tag: String? = null
): Lazy<TViewModel>
        where TViewModel : ViewModel,
              TFragment : KodeinAware,
              TFragment : Fragment {

    return threadUnsafeLazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>) =
                kodein.direct.instance<TViewModel>(tag) as T
        }).get(TViewModel::class.java)
        ViewModelProvider(this).get(TViewModel::class.java)
    }
}

/**
 * Example usage:
 *
 * Define your kodein binding as PROVIDER (since there is no argument):
 * ```kotlin
 *     bind<SomeViewModel>() with provider {
 *         SomeViewModel(instance(),... )
 *     }
 * ```
 * And then inject your view model in following manner:
 * ```kotlin
 *     class SomeActivity: FragmentActivity(), KodeinAware {
 *         val viewModel: SomeViewModel by viewModel()
 *     }
 * ```
 * It is IMPORTANT to define view model variable type, otherwise you have to define all methods generics - not so cool.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified TViewModel, TActivity> TActivity.viewModel(
    tag: String? = null
): Lazy<TViewModel>
        where TViewModel : ViewModel,
              TActivity : KodeinAware,
              TActivity : FragmentActivity {

    return threadUnsafeLazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>) =
                kodein.direct.instance<TViewModel>(tag) as T
        }).get(TViewModel::class.java)
    }
}
