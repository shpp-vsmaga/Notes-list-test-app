package com.noteslist.app.common.utils

import android.app.Activity
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.noteslist.app.common.ui.SimpleTextWatcher

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

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.showIf(condition: Boolean) = if (condition) show() else hide()
fun View.visibleIf(condition: Boolean) = if (condition) setVisible() else setInvisible()

fun View.enableIf(condition: Boolean) = if (condition) enable() else disable()

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun EditText.replaceLengthFilter(filter: InputFilter.LengthFilter?) {
    filters = mutableListOf(*filters).apply {
        val indexOfLengthFilter = indexOfFirst { it is InputFilter.LengthFilter }
        if (indexOfLengthFilter != -1) {
            removeAt(indexOfLengthFilter)
        }
        filter?.let { filter -> add(filter) }
    }.toTypedArray()
}

fun EditText.removeLengthFilter() = replaceLengthFilter(null)

fun EditText.afterTextChanged(callback: (String) -> Unit) =
    this.addTextChangedListener(object : SimpleTextWatcher() {
        override fun afterTextChanged(text: Editable) {
            super.afterTextChanged(text)
            callback(text.toString())
        }
    })

fun EditText.clear() {
    setText("")
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

fun DialogFragment.changeStatusBarColor(
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

fun Fragment.showMessage(text: String?) {
    text?.let {
        this.view?.let { view ->
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }
    }
}

fun BottomSheetDialogFragment.showMessage(text: String?) {
    text?.let {
        this.view?.let { view ->
            Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
        }
    }
}

fun Fragment.showToastMessage(text: String?) {
    text?.let {
        this.context?.let { context ->
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

fun Activity.showMessage(text: String?) {
    text?.let {
        Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_SHORT).show()
    }
}

fun ImageView.setGrayScale() {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f) //0 means grayscale

    val cf = ColorMatrixColorFilter(matrix)
    colorFilter = cf
    imageAlpha = 128 // 128 = 0.5
}