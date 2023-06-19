package com.chooloo.www.chooloolib.interactor.animation

import android.animation.ValueAnimator.REVERSE
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimationsInteractorImpl @Inject constructor(
    private val preferencesInteractor: PreferencesInteractor
) :
    BaseObservable<AnimationsInteractor.Listener>(),
    AnimationsInteractor {

    private val _isEnabled: Boolean
        get() = preferencesInteractor.isAnimations


    override fun focus(view: View) {
        if (_isEnabled) {
            YoYo.with(Techniques.FadeIn)
                .duration(800)
                .repeatMode(REVERSE)
                .repeat(Animation.INFINITE)
                .playOn(view)
        }
    }

    override fun show(view: View, ifGone: Boolean) {
        if (view.isVisible && ifGone) {
            return
        }
        view.visibility = VISIBLE
        if (_isEnabled) {
            YoYo.with(Techniques.SlideInDown)
                .duration(100)
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
            YoYo.with(Techniques.FadeOutDown)
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
                    R.anim.layout_animation_fade_in
                )
                scheduleLayoutAnimation()
            }
        }
    }
}