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
    private val _isEnabled by lazy { KolerPreferences(context).animations }


    fun showView(view: View, isShow: Boolean) {
        if (_isEnabled) {
            if (isShow && view.visibility != VISIBLE) {
                YoYo.with(Techniques.FadeInUp)
                    .duration(400)
                    .playOn(view)
            } else if (!isShow && view.visibility == VISIBLE) {
                YoYo.with(Techniques.FadeOutDown)
                    .duration(400)
                    .onEnd { view.visibility = GONE }
                    .playOn(view)
            }
        } else {
            view.visibility = if (isShow) VISIBLE else GONE
        }
    }

    fun bounceIn(view: View) {
        view.visibility = VISIBLE
        if (_isEnabled) {
            YoYo.with(Techniques.BounceInUp)
                .duration(400)
                .playOn(view)
        }
    }

    fun tada(view: View) {
        if (_isEnabled) {
            YoYo.with(Techniques.Tada)
                .duration(200)
                .playOn(view)
        }
    }

    fun blink(view: View, totalDuration: Long, duration: Long = 400) {
        if (_isEnabled) {
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
        if (_isEnabled) {
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