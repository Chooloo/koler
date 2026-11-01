package com.chooloo.www.chooloolib.ui.dialpad

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardBackspace
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.DialpadTextField
import com.chooloo.www.chooloolib.ui.components.Keypad
import com.chooloo.www.chooloolib.ui.theme.Spacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Dialpad(
    modifier: Modifier = Modifier,
    value: String = "",
    onCallClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDeleteLongClick: () -> Unit = {},
    onAddContactClick: () -> Unit = {},
    onKeyClick: (key: Char) -> Unit = {},
) {
    val viewConfiguration = LocalViewConfiguration.current
    val deleteInteractionSource = remember { MutableInteractionSource() }

    LaunchedEffect(key1 = deleteInteractionSource) {
        var isLongClick = false

        deleteInteractionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
                    onDeleteLongClick()
                }

                is PressInteraction.Release -> {
                    if (!isLongClick) {
                        onDeleteClick()
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        DialpadTextField(text = value)

        Keypad(onKeyClick = onKeyClick)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Spacing.Medium,
                    horizontal = Spacing.ExtraLarge
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = onAddContactClick,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .aspectRatio(1f),
                        colors = IconButtonDefaults.outlinedIconButtonColors(),
                    ) {
                                Icon(
                                    Icons.Outlined.PersonAdd,
                                    stringResource(R.string.cd_add_contact)
                                )
                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onCallClick,
                    colors = IconButtonDefaults.filledIconButtonColors(),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                ) {
                    Icon(Icons.Rounded.Call, stringResource(R.string.cd_call))
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .aspectRatio(1f),
                        interactionSource = deleteInteractionSource,
                        colors = IconButtonDefaults.outlinedIconButtonColors()
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.KeyboardBackspace,
                            stringResource(R.string.cd_backspace)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DialerPreview() {
    Dialpad(value = "234324")
}
