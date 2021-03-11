package com.chooloo.www.koler.util

import android.os.Handler
import android.view.View
import android.view.View.INVISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R

fun showView(view: View, isShow: Boolean) {
    view.visibility = if (isShow) View.VISIBLE else View.GONE
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

fun setFadeAnimation(view: View) {
    view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.animation_fade_in))
}

fun setFadeUpAnimation(view: View) {
    view.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.animation_fall_down_show))
}

fun runLayoutAnimation(recyclerView: RecyclerView) {
    recyclerView.layoutAnimation =
        AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_fall_down)
    recyclerView.adapter!!.notifyDataSetChanged()
    recyclerView.scheduleLayoutAnimation()
}

fun animateViews(views: Array<View>, delay: Int = 50, isShow: Boolean) {
    views.forEach { it.visibility = INVISIBLE }
    views.indices.forEach {
        val view = views[it]
        view.postDelayed({
            view.visibility = View.VISIBLE
            showView(view, isShow)
        }, it * delay.toLong())
    }
}

fun blinkView(view: View, duration: Long, totalDuration: Long) {
    view.startAnimation(AlphaAnimation(0.0f, 1.0f).apply {
        setDuration(duration)
        startOffset = 20
        repeatMode = Animation.REVERSE
        repeatCount = Animation.INFINITE
    })
    Handler().postDelayed({ view.clearAnimation() }, totalDuration)
}