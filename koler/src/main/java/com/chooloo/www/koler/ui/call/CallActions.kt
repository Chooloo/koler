package com.chooloo.www.koler.ui.call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun CallActions(
    actions: List<CallAction>,
    onActionClick: (CallAction) -> Unit = {},
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(actions, key = { it.type }) { action ->
            CallActionButton(action = action, onClick = { onActionClick(action) })
        }
    }
}

@Composable
private fun CallActionButton(action: CallAction, onClick: () -> Unit) {
    val (icon, textRes) = action.appearance()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small),
        modifier = Modifier.padding(Spacing.Medium)
    ) {
        FilledTonalIconButton(
            onClick = onClick,
            enabled = action.isEnabled,
            colors = if (action.isActive) {
                IconButtonDefaults.filledIconButtonColors()
            } else {
                IconButtonDefaults.filledTonalIconButtonColors()
            }
        ) {
            Icon(imageVector = icon, contentDescription = stringResource(textRes))
        }
        Text(text = stringResource(textRes), style = MaterialTheme.typography.labelSmall)
    }
}

private fun CallAction.appearance(): Pair<ImageVector, Int> = when (type) {
    CallActionType.KEYPAD -> Icons.Rounded.Dialpad to R.string.call_action_keypad
    CallActionType.ADD_CALL -> Icons.Rounded.AddIcCall to R.string.call_action_add_call
    CallActionType.SWAP -> Icons.Rounded.SwapCalls to R.string.call_action_swap
    CallActionType.MERGE -> Icons.AutoMirrored.Rounded.CallMerge to R.string.call_action_merge
    CallActionType.MUTE -> if (isActive) Icons.Rounded.Mic to R.string.call_action_unmute else Icons.Rounded.MicOff to R.string.call_action_mute
    CallActionType.HOLD -> if (isActive) Icons.Rounded.PlayArrow to R.string.call_action_resume else Icons.Rounded.Pause to R.string.call_action_hold
    CallActionType.SPEAKER -> if (isActive) Icons.AutoMirrored.Rounded.VolumeUp to R.string.call_action_speaker_off else Icons.AutoMirrored.Rounded.VolumeDown to R.string.call_action_speaker
    CallActionType.ACCEPT, CallActionType.DENY, CallActionType.MANAGE -> error("$type is not a grid action")
}
