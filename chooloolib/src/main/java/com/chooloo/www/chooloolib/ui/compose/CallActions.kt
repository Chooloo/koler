package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallMerge
import androidx.compose.material.icons.automirrored.rounded.VolumeDown
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.AddIcCall
import androidx.compose.material.icons.rounded.Dialpad
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SwapCalls
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.runtime.Composable
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.CallActionData
import com.chooloo.www.chooloolib.domain.model.CallActionType

@Composable
fun CallActions(
    visibleActions: List<CallActionType> = listOf(),
    enabledActions: List<CallActionType> = listOf(),
    activatedActions: List<CallActionType> = listOf(),
    onActionClick: (action: CallActionType) -> Unit = {},
) {
    fun getActionData(action: CallActionType) = when (action) {
        CallActionType.SWAP -> CallActionData(
            action = CallActionType.SWAP,
            icon = Icons.Rounded.SwapCalls,
            text = R.string.call_action_swap,
            isEnabled = action in enabledActions,
            isActivated = action in activatedActions
        )

        CallActionType.MERGE -> CallActionData(
            action = CallActionType.MERGE,
            icon = Icons.AutoMirrored.Rounded.CallMerge,
            text = R.string.call_action_merge,
            isEnabled = action in enabledActions,
            isActivated = action in activatedActions
        )

        CallActionType.KEYPAD -> CallActionData(
            icon = Icons.Rounded.Dialpad,
            action = CallActionType.KEYPAD,
            text = R.string.call_action_keypad,
            isEnabled = action in enabledActions,
            isActivated = action in activatedActions
        )

        CallActionType.MUTE_ON -> CallActionData(
            icon = Icons.Rounded.MicOff,
            action = CallActionType.MUTE_ON,
            text = R.string.call_action_mute,
            activatedIcon = Icons.Rounded.Mic,
            isEnabled = action in enabledActions,
            counterAction = CallActionType.MUTE_OFF,
            isActivated = action in activatedActions,
            activatedText = R.string.call_action_unmute,
        )

        CallActionType.HOLD_ON -> CallActionData(
            icon = Icons.Rounded.Pause,
            action = CallActionType.HOLD_ON,
            text = R.string.call_action_hold,
            isEnabled = action in enabledActions,
            counterAction = CallActionType.HOLD_OFF,
            activatedIcon = Icons.Rounded.PlayArrow,
            isActivated = action in activatedActions,
            activatedText = R.string.call_action_resume,
        )

        CallActionType.MUTE_OFF -> CallActionData(
            icon = Icons.Rounded.MicOff,
            action = CallActionType.MUTE_ON,
            text = R.string.call_action_mute,
            activatedIcon = Icons.Rounded.Mic,
            isEnabled = action in enabledActions,
            counterAction = CallActionType.MUTE_OFF,
            isActivated = action in activatedActions,
            activatedText = R.string.call_action_unmute,
        )

        CallActionType.ADD_CALL -> CallActionData(
            icon = Icons.Rounded.AddIcCall,
            action = CallActionType.ADD_CALL,
            text = R.string.call_action_add_call,
            isEnabled = action in enabledActions,
            isActivated = action in activatedActions
        )

        CallActionType.HOLD_OFF -> CallActionData(
            icon = Icons.Rounded.Pause,
            action = CallActionType.HOLD_ON,
            text = R.string.call_action_hold,
            isEnabled = action in enabledActions,
            counterAction = CallActionType.HOLD_OFF,
            activatedIcon = Icons.Rounded.PlayArrow,
            isActivated = action in activatedActions,
            activatedText = R.string.call_action_resume,
        )

        CallActionType.SPEAKER_ON -> CallActionData(
            action = CallActionType.SPEAKER_ON,
            isEnabled = action in enabledActions,
            icon = Icons.AutoMirrored.Rounded.VolumeDown,
            activatedIcon = Icons.AutoMirrored.Rounded.VolumeUp,
            text = R.string.call_action_speaker_off,
            isActivated = action in activatedActions,
            counterAction = CallActionType.SPEAKER_OFF,
            activatedText = R.string.call_action_speaker_off,
        )

        CallActionType.SPEAKER_OFF -> CallActionData(
            icon = Icons.AutoMirrored.Rounded.VolumeDown,
            action = CallActionType.SPEAKER_ON,
            isEnabled = action in enabledActions,
            activatedIcon = Icons.AutoMirrored.Rounded.VolumeUp,
            text = R.string.call_action_speaker_off,
            isActivated = action in activatedActions,
            counterAction = CallActionType.SPEAKER_OFF,
            activatedText = R.string.call_action_speaker_off,
        )

        CallActionType.DENY -> null
        CallActionType.ACCEPT -> null
        CallActionType.MANAGE -> null
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        visibleActions.forEach { action ->
            item(key = action) {
                getActionData(action)?.let { actionData ->
                    CallAction(callActionData = actionData, onClick = { onActionClick(action) })
                }
            }
        }
    }
}