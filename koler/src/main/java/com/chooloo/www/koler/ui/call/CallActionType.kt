package com.chooloo.www.koler.ui.call

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallMerge
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.AddIcCall
import androidx.compose.material.icons.rounded.Dialpad
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.SwapCalls
import androidx.compose.ui.graphics.vector.ImageVector
import com.chooloo.www.chooloolib.R

enum class CallActionType(val icon: ImageVector, @StringRes val labelRes: Int) {
    KEYPAD(Icons.Rounded.Dialpad, R.string.call_action_keypad),
    MUTE(Icons.Rounded.MicOff, R.string.call_action_mute),
    HOLD(Icons.Rounded.Pause, R.string.call_action_hold),
    ADD_CALL(Icons.Rounded.AddIcCall, R.string.call_action_add_call),
    SPEAKER(Icons.AutoMirrored.Rounded.VolumeUp, R.string.call_action_speaker),
    SWAP(Icons.Rounded.SwapCalls, R.string.call_action_swap),
    MERGE(Icons.AutoMirrored.Rounded.CallMerge, R.string.call_action_merge),
}
