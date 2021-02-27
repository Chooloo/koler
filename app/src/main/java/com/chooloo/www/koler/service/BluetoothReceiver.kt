package com.chooloo.www.koler.service

import android.bluetooth.BluetoothAdapter.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.MODE_IN_CALL
import android.media.AudioManager.MODE_NORMAL

class BluetoothReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
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
                            mode = 0
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