package com.noteslist.app.common.utils

import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

private const val CLICK_DELAY_MILLIS = 500L

fun View.setThrottleOnClickListener(callback: (view: View) -> Unit) {
    var lastClickTime = 0L

    this.setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - lastClickTime > CLICK_DELAY_MILLIS) {
            lastClickTime = currentTimeMillis
            callback.invoke(it)
        }
    }
}

fun Fragment.showMessage(text: String?) {
    text?.let {
        this.view?.let { view ->
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }
    }
}

fun Fragment.changeStatusBarColor(
    activity: FragmentActivity?, @ColorInt color: Int,
    lightStatusBar: Boolean
) {
    activity?.window?.let { win ->
        val oldFlags = win.decorView.systemUiVisibility
        win.statusBarColor = color

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = oldFlags
            flags = if (lightStatusBar) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            win.decorView.systemUiVisibility = flags
        }
    }
}
