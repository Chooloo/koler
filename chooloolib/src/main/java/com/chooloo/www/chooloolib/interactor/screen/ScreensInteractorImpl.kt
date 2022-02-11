package com.chooloo.www.chooloolib.interactor.screen

import android.app.KeyguardManager
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.view.WindowManager.LayoutParams.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ScreensInteractorImpl @Inject constructor(
    private val keyguardManager: KeyguardManager,
    @ActivityContext private val context: Context,
    private val inputMethodManager: InputMethodManager
) : BaseObservable<ScreensInteractor.Listener>(), ScreensInteractor {

    private val activity = context as BaseActivity<*>


    override fun disableKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardManager.requestDismissKeyguard(activity, null)
        } else {
            activity.window.addFlags(FLAG_DISMISS_KEYGUARD)
        }
    }

    override fun setShowWhenLocked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            activity.apply {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
                keyguardManager.requestDismissKeyguard(this, null)
            }
        } else {
            activity.window.addFlags(FLAG_SHOW_WHEN_LOCKED or FLAG_TURN_SCREEN_ON or FLAG_DISMISS_KEYGUARD)
        }
    }

    override fun ignoreEditTextFocus(event: MotionEvent) {
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