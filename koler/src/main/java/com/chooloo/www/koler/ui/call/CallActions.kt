package com.chooloo.www.koler.ui.call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallMerge
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.AddIcCall
import androidx.compose.material.icons.rounded.Dialpad
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.SwapCalls
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.RoundAction
import com.chooloo.www.chooloolib.ui.theme.Sizes
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun CallActions(
    actions: List<CallAction>,
    onActionClick: (CallActionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Large),
        modifier = modifier.fillMaxWidth(),
    ) {
        actions.chunked(ACTIONS_PER_ROW).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
                row.forEach { action ->
                    val (icon, labelRes) = action.appearance()
                    RoundAction(
                        icon = icon,
                        label = stringResource(labelRes),
                        onClick = { onActionClick(action.type) },
                        isHighlighted = action.isActive,
                        enabled = action.isEnabled,
                        size = Sizes.ButtonLarge,
                        modifier = Modifier.width(ACTION_WIDTH),
                    )
                }
            }
        }
    }
}

private fun CallAction.appearance(): Pair<ImageVector, Int> = when (type) {
    CallActionType.KEYPAD -> Icons.Rounded.Dialpad to R.string.call_action_keypad
    CallActionType.ADD_CALL -> Icons.Rounded.AddIcCall to R.string.call_action_add_call
    CallActionType.SWAP -> Icons.Rounded.SwapCalls to R.string.call_action_swap
    CallActionType.MERGE -> Icons.AutoMirrored.Rounded.CallMerge to R.string.call_action_merge
    CallActionType.MUTE -> Icons.Rounded.MicOff to R.string.call_action_mute
    CallActionType.HOLD -> Icons.Rounded.Pause to R.string.call_action_hold
    CallActionType.SPEAKER -> Icons.AutoMirrored.Rounded.VolumeUp to R.string.call_action_speaker
}

private const val ACTIONS_PER_ROW = 3
private val ACTION_WIDTH = Sizes.ButtonLarge + Spacing.Large
