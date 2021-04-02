package com.chooloo.www.koler.util

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.View.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.preferences.KolerPreferences

class AnimationManager(private val context: Context) {
    private val isEnabled by lazy { KolerPreferences(context).isAnimations }

    fun showView(view: View, isShow: Boolean) {
        if (view.visibility == (if (isShow) VISIBLE else GONE)) {
            return
        }
        view.visibility = if (isShow) VISIBLE else GONE
        if (isEnabled) {
            view.startAnimation(
                AnimationUtils.loadAnimation(
                    view.context,
                    if (isShow) {
                        R.anim.animation_fall_down_show
                    } else {
                        R.anim.animation_fall_down_hide
                    }
                )
            )
        }
    }

    fun setFadeAnimation(view: View) {
        if (isEnabled) {
            view.startAnimation(
                AnimationUtils.loadAnimation(
                    view.context,
                    R.anim.animation_fade_in
                )
            )
        }
    }

    fun setFadeUpAnimation(view: View) {
        if (isEnabled) {
            view.startAnimation(
                AnimationUtils.loadAnimation(
                    view.context,
                    R.anim.animation_fall_down_show
                )
            )
        }
    }

    fun runLayoutAnimation(recyclerView: RecyclerView) {
        if (isEnabled) {
            recyclerView.apply {
                layoutAnimation = AnimationUtils.loadLayoutAnimation(
                    recyclerView.context,
                    R.anim.layout_animation_fall_down
                )
                adapter!!.notifyDataSetChanged()
                scheduleLayoutAnimation()
            }
        }
    }

    fun animateViews(views: Array<View>, delay: Int = 50, isShow: Boolean) {
        if (isEnabled) {
            views.forEach { it.visibility = INVISIBLE }
            views.indices.forEach {
                val view = views[it]
                view.postDelayed({
                    view.visibility = View.VISIBLE
                    AnimationManager(context).showView(view, isShow)
                }, it * delay.toLong())
            }
        }
    }

    fun blinkView(view: View, totalDuration: Long, duration: Long = 400) {
        if (isEnabled) {
            view.startAnimation(AlphaAnimation(0.0f, 1.0f).apply {
                setDuration(duration)
                startOffset = 20
                repeatMode = Animation.REVERSE
                repeatCount = Animation.INFINITE
            })
            Handler().postDelayed({ view.clearAnimation() }, totalDuration)
        }
    }
}