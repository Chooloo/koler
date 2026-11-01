package com.chooloo.www.koler.ui.call

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.CircleImage
import com.chooloo.www.chooloolib.ui.theme.AvatarSize
import com.chooloo.www.chooloolib.ui.theme.Spacing
import com.chooloo.www.koler.domain.CallRepository.CallsState

@Composable
fun Call(
    callerName: String,
    stateText: String? = null,
    elapsedSeconds: Long? = null,
    callerImagePainter: Painter? = null,
    callsState: CallsState = CallsState.UNKNOWN,
    visibleCallActions: List<CallActionType> = listOf(),
    enabledCallActions: List<CallActionType> = listOf(),
    activatedCallActions: List<CallActionType> = listOf(),
    onCallActionClick: (action: CallActionType) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.ExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = Spacing.ExtraLarge)
        ) {
            elapsedSeconds?.let {
                Text(
                    text = DateUtils.formatElapsedTime(it),
                    style = typography.labelSmall
                )
            }

            stateText?.let {
                Text(
                    text = it,
                    style = typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(Spacing.Medium))

            callerImagePainter?.let {
                CircleImage(
                    painter = it,
                    modifier = Modifier.size(AvatarSize.Large)
                )
                Spacer(modifier = Modifier.height(Spacing.Medium))
            }

            Text(
                text = callerName,
                style = typography.headlineLarge
            )
        }

        if (callsState in listOf(CallsState.ACTIVE, CallsState.ACTIVE_MULTI)) {
            CallActions(
                onActionClick = onCallActionClick,
                visibleActions = visibleCallActions,
                enabledActions = enabledCallActions,
                activatedActions = activatedCallActions,
            )
        } else {
            Spacer(modifier = Modifier.height(Spacing.Medium))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.ExtraLarge),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (callsState in listOf(CallsState.INCOMING, CallsState.EXTRA_INCOMING)) {
                FloatingActionButton(
                    onClick = { onCallActionClick(CallActionType.ACCEPT) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Call,
                        contentDescription = stringResource(R.string.cd_accept_call)
                    )
                }
            }

            if (callsState == CallsState.ACTIVE_MULTI) {
                FloatingActionButton(onClick = { onCallActionClick(CallActionType.MANAGE) }) {
                    Icon(
                        imageVector = Icons.Rounded.PeopleAlt,
                        contentDescription = stringResource(R.string.cd_manage_call)
                    )
                }
            }

            if (callsState !in listOf(CallsState.DISCONNECTED, CallsState.NO_CALLS)) {
                FloatingActionButton(
                    onClick = { onCallActionClick(CallActionType.DENY) },
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CallEnd,
                        contentDescription = stringResource(R.string.cd_hangup_call)
                    )
                }
            }
        }
    }
}
