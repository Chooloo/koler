package com.chooloo.www.koler.ui.call

import android.annotation.SuppressLint
import android.app.ActivityManager
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.chooloolib.utils.showWhenLocked
import com.chooloo.www.koler.service.CallService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity() {
    @set:Inject var callService: CallService? = null

    override fun onSetup() {
        setExcludeFromRecents(false)
        callService?.bindActivity(this)
        showWhenLocked()

        setChoolooContent {
            CallScreen()
        }
    }

    override fun onDestroy() {
        setExcludeFromRecents(true)
        callService?.unbindActivity(this)
        super.onDestroy()
    }

    private fun setExcludeFromRecents(exclude: Boolean) {
        getSystemService(ActivityManager::class.java).appTasks.firstOrNull { it.taskInfo?.taskId == taskId }
            ?.setExcludeFromRecents(exclude)
    }

    companion object {
        const val SHOW_DIALPAD_EXTRA = "show_dialpad"
    }
}
