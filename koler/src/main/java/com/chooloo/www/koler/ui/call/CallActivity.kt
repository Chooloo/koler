package com.chooloo.www.koler.ui.call

import android.app.KeyguardManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.koler.domain.CallRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallActivity : BaseActivity() {
    @Inject lateinit var callRepository: CallRepository

    private val callViewModel: CallViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showWhenLocked()
        showKeypadIfRequested(intent)
        finishWhenCallsEnd()
        setChoolooContent {
            CallScreen(callViewModel = callViewModel)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        showKeypadIfRequested(intent)
    }

    override fun finish() {
        finishAndRemoveTask()
    }

    private fun finishWhenCallsEnd() {
        lifecycleScope.launch {
            callRepository.calls.first { it.isEmpty() }
            finish()
        }
    }

    private fun showWhenLocked() {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        getSystemService(KeyguardManager::class.java).requestDismissKeyguard(this, null)
    }

    private fun showKeypadIfRequested(intent: Intent) {
        if (intent.getBooleanExtra(EXTRA_SHOW_DIALPAD, false)) callViewModel.onShowKeypad()
    }

    companion object {
        const val EXTRA_SHOW_DIALPAD = "show_dialpad"
    }
}
