package com.chooloo.www.koler.interactor.animation

import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor
import com.chooloo.www.koler.util.BaseObservable
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class AnimationInteractorImpl(
    private val preferencesInteractor: PreferencesInteractor
) :
    BaseObservable<AnimationInteractor.Listener>(),
    AnimationInteractor {

    private val _isEnabled: Boolean
        get() = preferencesInteractor.isAnimations

    override fun showView(view: View, isShow: Boolean) {
        if (_isEnabled) {
            if (isShow && view.visibility != View.VISIBLE) {
                YoYo.with(Techniques.FadeInUp)
                    .duration(400)
                    .playOn(view)
            } else if (!isShow && view.visibility == View.VISIBLE) {
                YoYo.with(Techniques.FadeOutDown)
                    .duration(400)
                    .onEnd { view.visibility = View.GONE }
                    .playOn(view)
            }
        } else {
            view.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    override fun animateIn(view: View) {
        view.visibility = View.VISIBLE
        if (_isEnabled) {
            YoYo.with(Techniques.BounceInUp)
                .duration(400)
                .playOn(view)
        }
    }

    override fun animateFocus(view: View) {
        if (_isEnabled) {
            YoYo.with(Techniques.Tada)
                .duration(200)
                .playOn(view)
        }
    }

    override fun animateBlink(view: View, totalDuration: Long, duration: Long) {
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

    override fun animateRecyclerView(recyclerView: RecyclerView) {
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