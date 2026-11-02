package com.chooloo.www.koler.ui.call

import android.app.ActivityManager
import android.app.KeyguardManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.koler.domain.CallRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallActivity : BaseActivity() {
    @Inject lateinit var callRepository: CallRepository

    private val callViewModel: CallViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setExcludeFromRecents(false)
        showWhenLocked()
        if (intent.getBooleanExtra(EXTRA_SHOW_DIALPAD, false)) callViewModel.onShowKeypad()
        finishWhenCallsEnd()
        setChoolooContent {
            CallScreen(callViewModel = callViewModel)
        }
    }

    override fun onDestroy() {
        setExcludeFromRecents(true)
        super.onDestroy()
    }

    override fun finish() {
        finishAndRemoveTask()
    }

    private fun finishWhenCallsEnd() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                callRepository.calls.collect { if (it.isEmpty()) finish() }
            }
        }
    }

    private fun showWhenLocked() {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        getSystemService(KeyguardManager::class.java).requestDismissKeyguard(this, null)
    }

    private fun setExcludeFromRecents(exclude: Boolean) {
        getSystemService(ActivityManager::class.java).appTasks
            .firstOrNull { it.taskInfo?.taskId == taskId }
            ?.setExcludeFromRecents(exclude)
    }

    companion object {
        const val EXTRA_SHOW_DIALPAD = "show_dialpad"
    }
}
