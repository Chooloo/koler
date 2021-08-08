package com.chooloo.www.koler.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

open class AllPurposeTouchListener(context: Context) : OnTouchListener {
    private val _gestureListener by lazy { GestureListener() }
    private val _gestureDetector by lazy { GestureDetector(context, _gestureListener) }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        _gestureListener.view = v
        return _gestureDetector.onTouchEvent(event)
    }

    open fun onSwipeTop() {}
    open fun onSwipeLeft() {}
    open fun onSwipeRight() {}
    open fun onSwipeBottom() {}
    open fun onLongPress(v: View?) {}
    open fun onSingleTapUp(v: View?) = false
    open fun onSingleTapConfirmed(v: View?) = false


    private inner class GestureListener : SimpleOnGestureListener() {
        private var _view: View? = null

        var view: View?
            get() = _view
            set(value) {
                _view = value
            }

        override fun onDown(e: MotionEvent) = true

        override fun onSingleTapConfirmed(e: MotionEvent) =
            this@AllPurposeTouchListener.onSingleTapConfirmed(_view)

        override fun onSingleTapUp(e: MotionEvent) =
            this@AllPurposeTouchListener.onSingleTapUp(_view)

        override fun onLongPress(e: MotionEvent) {
            this@AllPurposeTouchListener.onLongPress(_view)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ) = try {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x
            if (abs(diffX) > abs(diffY) && abs(diffX) > Companion.SWIPE_THRESHOLD && abs(velocityX) > Companion.SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) onSwipeRight() else onSwipeLeft()
                true
            } else if (abs(diffY) > Companion.SWIPE_THRESHOLD && abs(velocityY) > Companion.SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) onSwipeBottom() else onSwipeTop()
                true
            } else {
                false
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }


    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
}