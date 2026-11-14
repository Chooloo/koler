package com.chooloo.www.chooloolib.ui.dialpad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.DialpadNumber
import com.chooloo.www.chooloolib.ui.components.FloatingBubble
import com.chooloo.www.chooloolib.ui.components.Keypad
import com.chooloo.www.chooloolib.ui.components.Reveal
import com.chooloo.www.chooloolib.ui.theme.Sizes
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun Dialpad(
    modifier: Modifier = Modifier,
    value: String,
    onCallClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDeleteLongClick: () -> Unit,
    onAddContactClick: () -> Unit,
    onKeyClick: (key: Char) -> Unit,
    onKeyLongClick: ((key: Char) -> Unit)?,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        DialpadNumber(number = value)
        Keypad(onKeyClick = onKeyClick, onKeyLongClick = onKeyLongClick)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.Medium),
        ) {
            val sideOffset = Sizes.Button / 2 + Spacing.Medium + Sizes.ButtonLarge / 2
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
            ) {
                Reveal(visible = value.isNotEmpty(), hiddenOffsetX = sideOffset, hiddenScale = 1f) {
                    FloatingBubble(
                        icon = Icons.Rounded.PersonAdd,
                        contentDescription = stringResource(R.string.cd_add_contact),
                        onClick = onAddContactClick,
                        size = Sizes.Button,
                    )
                }
                FloatingBubble(
                    icon = Icons.Rounded.Call,
                    contentDescription = stringResource(R.string.cd_call),
                    onClick = onCallClick,
                    size = Sizes.ButtonLarge,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.zIndex(1f),
                )
                Reveal(
                    visible = value.isNotEmpty(),
                    hiddenOffsetX = -sideOffset,
                    hiddenScale = 1f,
                ) {
                    FloatingBubble(
                        icon = Icons.AutoMirrored.Rounded.Backspace,
                        contentDescription = stringResource(R.string.cd_backspace),
                        onClick = onDeleteClick,
                        onLongClick = onDeleteLongClick,
                        size = Sizes.Button,
                    )
                }
            }
        }
    }
}
