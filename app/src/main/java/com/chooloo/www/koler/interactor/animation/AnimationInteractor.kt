package com.chooloo.www.koler.interactor.animation

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface AnimationInteractor {
    interface Listener

    fun showView(view: View, isShow: Boolean)

    fun animateFocus(view: View)
    fun animateIn(view: View, ifGone: Boolean)
    fun animateBlink(view: View, totalDuration: Long, duration: Long = 400)
    fun animateOut(view: View, ifVisible: Boolean, goneOrInvisible: Boolean)

    fun animateRecyclerView(recyclerView: RecyclerView)
}