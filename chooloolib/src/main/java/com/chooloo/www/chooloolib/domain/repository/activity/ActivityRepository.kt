package com.chooloo.www.chooloolib.domain.repository.activity

import android.app.Activity
import android.view.MotionEvent
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface ActivityRepository : BaseRepository {
    fun showWhenLocked(activity: Activity)
    fun disableKeyboard(activity: Activity)
    fun ignoreEditTextFocus(activity: Activity, event: MotionEvent)
}