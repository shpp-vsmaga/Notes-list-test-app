package com.noteslist.app.common.utils

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator

fun NavController.navigateTo(
    direction: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions? = null,
    extras: FragmentNavigator.Extras? = null
) {
    currentDestination?.getAction(direction)
        ?.let { navigate(direction, bundle, navOptions, extras) }
}