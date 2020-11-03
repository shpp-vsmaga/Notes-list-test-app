package com.noteslist.app.common.ui

import android.animation.Animator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FabScrollBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {
    private var handler: Handler? = null
    private var isAnimating: Boolean = false

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        scheduleReturnTop(child)
    }

    private fun scheduleReturnTop(child: FloatingActionButton) {
        if (handler == null) handler = Handler(Looper.getMainLooper())
        handler?.postDelayed({
            val animator = child.animate().translationY(0f).setDuration(ANIMATION_DURATION)
                .setInterpolator(LinearInterpolator())
            animator.setListener(animationListener)
            animator.start()
        }, RESTORE_DELAY)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type
        )



        if (dyConsumed > SCROLL_REACTION_DELTA && child.translationY == 0f && !isAnimating) {
            val layoutParams =
                child.layoutParams as CoordinatorLayout.LayoutParams
            val fabBottomMargin = layoutParams.bottomMargin
            val animator = child.animate().translationY((child.height + fabBottomMargin).toFloat())
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(LinearInterpolator())
            animator.setListener(animationListener)
            animator.start()
        } else if (dyConsumed < -SCROLL_REACTION_DELTA && child.translationY != 0f && !isAnimating) {
            val animator = child.animate().translationY(0f).setDuration(ANIMATION_DURATION)
                .setInterpolator(LinearInterpolator())
            animator.setListener(animationListener)
            animator.start()
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        if (handler != null) {
            handler?.removeMessages(0)
        }
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    private val animationListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            isAnimating = true
        }

        override fun onAnimationEnd(animation: Animator?) {
            isAnimating = false
        }

        override fun onAnimationCancel(animation: Animator?) {
            isAnimating = false
        }

        override fun onAnimationStart(animation: Animator?) {
            isAnimating = true
        }
    }

    companion object {
        const val ANIMATION_DURATION = 200L
        const val RESTORE_DELAY = 800L
        const val SCROLL_REACTION_DELTA = 2
    }
}