package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.chooloo.www.chooloolib.R
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
                    if (isLongClick.not()) {
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.wrapContentWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_small)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_small)),
            contentPadding = PaddingValues(
                vertical = dimensionResource(id = R.dimen.spacing),
                horizontal = dimensionResource(id = R.dimen.spacing_big)
            ),
            content = {
                items(3) { index ->
                    when (index) {
                        0 -> if (value.isNotEmpty()) {
                            IconButton(
                                onClick = onAddContactClick::invoke,
                                modifier = Modifier.aspectRatio(1f),
                                colors = IconButtonDefaults.outlinedIconButtonColors(),
                            ) {
                                Icon(
                                    Icons.Outlined.PersonAdd,
                                    stringResource(R.string.add_person_cd)
                                )
                            }
                        }

                        1 ->
                            IconButton(
                                onClick = onCallClick::invoke,
                                colors = IconButtonDefaults.filledIconButtonColors(),
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .aspectRatio(1f)
                            ) {
                                Icon(Icons.Rounded.Call, stringResource(R.string.call_cd))
                            }

                        2 -> if (value.isNotEmpty()) {
                            IconButton(
                                onClick = { },
                                modifier = Modifier.aspectRatio(1f),
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
            },
        )

//        Row(
//            modifier = modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            if (value.isNotEmpty()) {
//                IconButton(
//                    onClick = onAddContactClick::invoke,
//                    colors = IconButtonDefaults.outlinedIconButtonColors(),
//                    modifier = Modifier
//                        .weight(0.3f)
//                        .aspectRatio(1f)
//                ) {
//                    Icon(Icons.Outlined.PersonAdd, stringResource(R.string.add_person_cd))
//                }
//            }
//
//            IconButton(
//                onClick = onCallClick::invoke,
//                colors = IconButtonDefaults.filledIconButtonColors(),
//                modifier = Modifier
//                    .fillMaxWidth(0.2f)
//                    .weight(0.3f)
//                    .aspectRatio(1f)
//            ) {
//                Icon(Icons.Rounded.Call, stringResource(R.string.call_cd))
//            }
//
//            if (value.isNotEmpty()) {
//                IconButton(
//                    onClick = { },
//                    interactionSource = deleteInteractionSource,
//                    colors = IconButtonDefaults.outlinedIconButtonColors(),
//                    modifier = Modifier
//                        .weight(0.3f)
//                        .aspectRatio(1f),
//                ) {
//                    Icon(
//                        Icons.AutoMirrored.Outlined.KeyboardBackspace,
//                        stringResource(R.string.cd_backspace)
//                    )
//                }
//            }
//        }
    }
}

@Preview
@Composable
fun DialerPreview() {
    Dialpad(value = "234324")
}