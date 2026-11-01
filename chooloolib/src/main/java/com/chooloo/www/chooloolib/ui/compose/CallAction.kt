package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.domain.model.CallActionData
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun CallAction(
    onClick: () -> Unit = {},
    callActionData: CallActionData,
) {
    val icon = when {
        !callActionData.isEnabled && callActionData.disabledIcon != null -> callActionData.disabledIcon
        callActionData.isActivated && callActionData.activatedIcon != null -> callActionData.activatedIcon
        else -> callActionData.icon
    }
    val textRes = when {
        !callActionData.isEnabled && callActionData.disabledText != null -> callActionData.disabledText
        callActionData.isActivated && callActionData.activatedText != null -> callActionData.activatedText
        else -> callActionData.text
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small),
        modifier = Modifier.padding(Spacing.Medium)
    ) {
        FilledTonalIconButton(
            onClick = onClick,
            enabled = callActionData.isEnabled,
            colors = if (callActionData.isActivated) {
                IconButtonDefaults.filledIconButtonColors()
            } else {
                IconButtonDefaults.filledTonalIconButtonColors()
            }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(textRes)
            )
        }
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
