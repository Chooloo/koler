package com.chooloo.www.koler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.chooloo.www.chooloolib.di.ApplicationScope
import com.chooloo.www.koler.domain.usecase.ClearMissedCalls
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MissedCallDismissReceiver : BroadcastReceiver() {
    @Inject lateinit var clearMissedCalls: ClearMissedCalls

    @Inject @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        applicationScope.launch {
            try {
                clearMissedCalls()
            } catch (error: Exception) {
                Log.e(TAG, "Failed clearing missed calls", error)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private companion object {
        const val TAG = "MissedCallDismissReceiver"
    }
}
