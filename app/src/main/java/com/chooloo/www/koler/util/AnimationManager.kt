package com.chooloo.www.koler.util

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.preferences.KolerPreferences
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class AnimationManager(private val context: Context) {
    companion object {
        private const val DEFAULT_DURATION = 500L
    }

    private val isEnabled by lazy { KolerPreferences(context).animations }

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

    fun bounceInUp(view: View) {
        if (isEnabled) {
            YoYo.with(Techniques.BounceInUp)
                .duration(400)
                .playOn(view)
        }
    }

    fun tada(view: View) {
        if (isEnabled) {
            YoYo.with(Techniques.Tada)
                .duration(200)
                .playOn(view)
        }
    }

    fun blink(view: View, totalDuration: Long, duration: Long = 400) {
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
}