package com.chooloo.www.chooloolib.receiver

import android.bluetooth.BluetoothAdapter.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.MODE_IN_CALL
import android.media.AudioManager.MODE_NORMAL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BluetoothBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var audioManager: AudioManager

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_STATE_CHANGED -> {
                when (intent.getIntExtra(EXTRA_STATE, ERROR)) {
                    STATE_CONNECTED -> {
                        audioManager.apply {
                            isBluetoothScoOn = false
                            stopBluetoothSco()
                            mode = MODE_NORMAL
                        }
                    }
                    else -> {
                        audioManager.apply {
                            isBluetoothScoOn = true
                            startBluetoothSco()
                            mode = MODE_IN_CALL
                        }
                    }
                }
            }
        }
    }
}