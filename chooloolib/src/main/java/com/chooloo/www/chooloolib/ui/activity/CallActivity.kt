package com.chooloo.www.chooloolib.ui.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.os.Build
import com.chooloo.www.chooloolib.data.service.CallService
import com.chooloo.www.chooloolib.ui.view.CallView
import com.chooloo.www.chooloolib.utils.showWhenLocked
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity() {
    @set:Inject var callService: CallService? = null
    @Inject lateinit var activityManager: ActivityManager

    override fun onSetup() {
        setExcludeFromRecents(false)
        callService?.bindActivity(this)
        showWhenLocked()

        setChoolooContent {
            CallView()
        }
    }

    override fun onDestroy() {
        setExcludeFromRecents(true)
        callService?.unbindActivity(this)
        super.onDestroy()
    }

    private fun setExcludeFromRecents(exclude: Boolean) {
        activityManager.appTasks.firstOrNull { it.taskInfo?.taskId == taskId }
            ?.setExcludeFromRecents(exclude)
    }

    companion object {
        const val SHOW_DIALPAD_EXTRA = "show_dialpad"
    }
}
