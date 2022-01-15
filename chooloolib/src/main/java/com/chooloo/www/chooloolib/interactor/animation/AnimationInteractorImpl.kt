package com.chooloo.www.chooloolib.interactor.animation

import android.animation.Animator
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class AnimationInteractorImpl(
    private val preferencesInteractor: PreferencesInteractor
) :
    BaseObservable<AnimationInteractor.Listener>(),
    AnimationInteractor {

    private val _isEnabled: Boolean
        get() = preferencesInteractor.isAnimations


    override fun focus(view: View) {
        if (_isEnabled) {
            YoYo.with(Techniques.Tada)
                .duration(200)
                .playOn(view)
        }
    }

    override fun show(view: View, ifGone: Boolean) {
        if (view.isVisible && ifGone) {
            return
        }
        view.visibility = VISIBLE
        if (_isEnabled) {
            YoYo.with(Techniques.BounceInUp)
                .duration(400)
                .playOn(view)
        }
    }

    override fun blink(view: View, totalDuration: Long, duration: Long) {
        if (_isEnabled) {
            view.startAnimation(AlphaAnimation(0.0f, 1.0f).apply {
                setDuration(duration)
                startOffset = 20
                repeatMode = Animation.REVERSE
                repeatCount = Animation.INFINITE
            })
            Handler(Looper.getMainLooper()).postDelayed({ view.clearAnimation() }, totalDuration)
        }
    }

    override fun hide(view: View, ifVisible: Boolean, goneOrInvisible: Boolean) {
        if (view.visibility != VISIBLE && ifVisible) {
            return
        }
        if (_isEnabled) {
            YoYo.with(Techniques.SlideOutUp)
                .duration(250)
                .onEnd {
                    view.visibility = if (goneOrInvisible) View.GONE else View.INVISIBLE
                }
                .playOn(view)
        } else {
            view.visibility = if (goneOrInvisible) View.GONE else View.INVISIBLE
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