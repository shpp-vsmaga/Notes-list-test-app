package com.noteslist.app.common.ui

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.marginBottom

class BottomListAnimator(private val mGroupLayout: View?) {
    fun hideViews() {
        if (mGroupLayout != null) {
            mGroupLayout.animate()
                .translationY(mGroupLayout.height.toFloat() + mGroupLayout.marginBottom)
                .setDuration(50).interpolator =
                AccelerateInterpolator(2f)
        }
    }

    fun showViews() {
        if (mGroupLayout != null) {
            mGroupLayout.animate().translationY(0f)
                .setDuration(50).interpolator = DecelerateInterpolator(2f)
        }
    }

}
