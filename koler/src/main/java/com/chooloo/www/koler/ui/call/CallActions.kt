package com.chooloo.www.koler.ui.call

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.ui.components.RoundAction
import com.chooloo.www.chooloolib.ui.theme.Layout
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
        actions.chunked(Layout.CALL_ACTIONS_PER_ROW).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)) {
                row.forEach { action ->
                    RoundAction(
                        icon = action.type.icon,
                        label = stringResource(action.type.labelRes),
                        onClick = { onActionClick(action.type) },
                        isHighlighted = action.isActive,
                        enabled = action.isEnabled,
                        size = Sizes.ButtonLarge,
                        modifier = Modifier.width(Sizes.CallActionWidth),
                    )
                }
            }
        }
    }
}
