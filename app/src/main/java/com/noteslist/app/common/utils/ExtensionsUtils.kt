package com.noteslist.app.common.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.core.content.ContextCompat

fun <T> threadUnsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Context.getDimen(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)
fun Context.getDimenPx(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

fun Context.toColor(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

@Suppress("UnsafeCallOnNullableType")
fun Context.toDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)!!

fun Context.toColorWithAlpha(@ColorRes resId: Int, ratio: Float) =
    ContextCompat.getColor(this, resId).addAlpha(ratio)

fun Activity.hideKeyboard() {
    this.currentFocus?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun EditText.showKeyboard() {
    requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Context.getPlural(@PluralsRes pluralRes: Int, count: Int): String =
    this.resources.getQuantityString(pluralRes, count, count)

fun Int.addAlpha(ratio: Float): Int {
    val newColor: Int
    val alpha = Math.round(Color.alpha(this) * ratio)
    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)
    newColor = Color.argb(alpha, r, g, b)
    return newColor
}

inline fun <T> T.applyIf(applyCondition: Boolean, block: T.() -> Unit): T {
    if (applyCondition) block(this)
    return this
}

/** Returns the int if it is not `null`, or 0 otherwise. */
fun Int?.orZero(): Int = this ?: 0

/** Returns the double if it is not `null`, or 0.0 otherwise. */
fun Double?.orZero(): Double = this ?: 0.0

/** Returns the double if it is not `null`, or 0.0 otherwise. */
fun Float?.orZero(): Float = this ?: 0f

/** Returns the boolean if it is not `null`, or false otherwise. */
fun Boolean?.orFalse(): Boolean = this ?: false

fun Int.toDp(context: Context?): Int =
    (this * context?.resources?.displayMetrics?.density.orZero()).toInt()

fun Int?.responseToBoolean(): Boolean =
    this == 1

fun Set<String>.equalsTo(other: Set<String>): Boolean {
    if (size != other.size) {
        return false
    }
    for (item in this) {
        if (!other.contains(item)) {
            return false
        }
    }
    return true
}

/**
 * Returns the second element, or `null` if the list size < 2.
 */
fun <T> List<T>.secondOrNull(): T? {
    return if (size < 2) null else this[1]
}
