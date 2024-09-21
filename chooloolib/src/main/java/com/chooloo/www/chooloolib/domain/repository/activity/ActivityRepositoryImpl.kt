package com.chooloo.www.chooloolib.domain.repository.activity

import android.app.Activity
import android.app.KeyguardManager
import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
import android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
import android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ActivityRepositoryImpl @Inject constructor(
    private val keyguardManager: KeyguardManager,
    private val inputMethodManager: InputMethodManager
) : BaseRepositoryImpl(), ActivityRepository {
    override fun disableKeyboard(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardManager.requestDismissKeyguard(activity, null)
        } else {
            @Suppress("DEPRECATION")
            activity.window.addFlags(FLAG_DISMISS_KEYGUARD)
        }
    }

    override fun showWhenLocked(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            activity.apply {
                setTurnScreenOn(true)
                setShowWhenLocked(true)
                keyguardManager.requestDismissKeyguard(this, null)
            }
        } else {
            @Suppress("DEPRECATION")
            activity.window.addFlags(FLAG_SHOW_WHEN_LOCKED or FLAG_TURN_SCREEN_ON or FLAG_DISMISS_KEYGUARD)
        }
    }

    override fun ignoreEditTextFocus(activity: Activity, event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = activity.currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
    }
}