package com.chooloo.www.chooloolib.ui.compose

import android.text.format.DateUtils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImagePainter
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.CallActionType
import com.chooloo.www.chooloolib.domain.repository.call.CallRepository.CallsState

@Composable
fun Call(
    callerName: String,
    stateText: String? = null,
    elapsedSeconds: Long? = null,
    callerImagePainter: AsyncImagePainter? = null,
    callsState: CallsState = CallsState.UNKNOWN,
    visibleCallActions: List<CallActionType> = listOf(),
    enabledCallActions: List<CallActionType> = listOf(),
    activatedCallActions: List<CallActionType> = listOf(),
    onCallActionClick: (action: CallActionType) -> Unit = {},
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

    callerImagePainter?.let {
        CircleImage(painter = it)
    }

    Text(
        text = callerName,
        style = typography.headlineLarge
    )

    if (callsState in listOf(CallsState.ACTIVE, CallsState.ACTIVE_MULTI)) {
        CallActions(
            onActionClick = onCallActionClick,
            visibleActions = visibleCallActions,
            enabledActions = enabledCallActions,
            activatedActions = activatedCallActions,
        )
    }

    if (callsState === CallsState.INCOMING) {
        FloatingActionButton(onClick = { onCallActionClick(CallActionType.ACCEPT) }) {
            Icon(
                imageVector = Icons.Rounded.Call,
                contentDescription = stringResource(R.string.cd_accept_call)
            )
        }
    }

    if (callsState === CallsState.ACTIVE_MULTI)
        FloatingActionButton(onClick = { onCallActionClick(CallActionType.MANAGE) }) {
            Icon(
                imageVector = Icons.Rounded.PeopleAlt,
                contentDescription = stringResource(R.string.cd_manage_call)
            )
        }

    if (callsState !in listOf(CallsState.DISCONNECTED, CallsState.NO_CALLS)) {
        FloatingActionButton(onClick = { onCallActionClick(CallActionType.DENY) }) {
            Icon(
                imageVector = Icons.Rounded.CallEnd,
                contentDescription = stringResource(R.string.cd_hangup_call)
            )
        }
    }
}