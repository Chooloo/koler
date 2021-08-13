package com.chooloo.www.koler.interactor.animation

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface AnimationInteractor {
    interface Listener
    
    fun showView(view: View, isShow: Boolean)

    fun animateIn(view: View)
    fun animateFocus(view: View)
    fun animateBlink(view: View, totalDuration: Long, duration: Long = 400)

    fun animateRecyclerView(recyclerView: RecyclerView)
}