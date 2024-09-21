package com.chooloo.www.chooloolib.ui.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.os.Build
import androidx.activity.compose.setContent
import com.chooloo.www.chooloolib.domain.repository.activity.ActivityRepository
import com.chooloo.www.chooloolib.domain.service.CallService
import com.chooloo.www.chooloolib.ui.view.CallView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity() {
    @set:Inject var callService: CallService? = null
    @Inject lateinit var activityManager: ActivityManager
    @Inject lateinit var activityRepository: ActivityRepository

    override fun onSetup() {
        setExcludeFromRecents(false)
        callService?.bindActivity(this)
        activityRepository.showWhenLocked(this)

        setContent {
            CallView()
        }
    }

    override fun onDestroy() {
        setExcludeFromRecents(true)
        window.exitTransition = null
        window.enterTransition = null
        callService?.unbindActivity(this)
        super.onDestroy()
    }

    private fun setExcludeFromRecents(exclude: Boolean) {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.appTasks
        val taskId = taskId
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityManager.appTasks.firstOrNull { it.taskInfo.taskId == taskId }
                ?.setExcludeFromRecents(exclude)
        } else {
            for (i in tasks.indices) {
                val task = tasks[i]
                @Suppress("DEPRECATION")
                if (task.taskInfo.id == taskId) {
                    try {
                        task.setExcludeFromRecents(exclude)
                    } catch (_: RuntimeException) {
                    }
                }
            }
        }
    }

    companion object {
        const val SHOW_DIALPAD_EXTRA = "show_dialpad"
    }
}
