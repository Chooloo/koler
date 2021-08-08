package com.chooloo.www.koler.util

import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.view.WindowManager.LayoutParams.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.koler.ui.base.BaseActivity

class ScreenManager(private val _activity: BaseActivity) {
    fun setShowWhenLocked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            _activity.apply {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
                (getSystemService(KEYGUARD_SERVICE) as KeyguardManager).requestDismissKeyguard(
                    this,
                    null
                )
            }
        } else {
            _activity.window.addFlags(FLAG_SHOW_WHEN_LOCKED or FLAG_TURN_SCREEN_ON or FLAG_DISMISS_KEYGUARD)
        }
    }

    fun disableKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (_activity.getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as KeyguardManager).requestDismissKeyguard(
                _activity,
                null
            )
        } else {
            _activity.window.addFlags(FLAG_DISMISS_KEYGUARD)
        }
    }

    fun ignoreEditTextFocus(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = _activity.currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm =
                        _activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
    }
}