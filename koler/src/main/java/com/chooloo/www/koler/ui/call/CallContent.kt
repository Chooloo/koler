package com.chooloo.www.koler.ui.call

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.PeopleAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.Avatar
import com.chooloo.www.chooloolib.ui.components.FloatingBubble
import com.chooloo.www.chooloolib.ui.theme.Sizes
import com.chooloo.www.chooloolib.ui.theme.Spacing
import com.chooloo.www.koler.domain.enum.CallsState

@Composable
fun CallContent(
    callerName: String,
    avatarName: String?,
    status: String?,
    callerPhotoUri: String?,
    callsState: CallsState,
    actions: List<CallAction>,
    onActionClick: (CallActionType) -> Unit,
    onAccept: () -> Unit,
    onHangup: () -> Unit,
    onManage: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val header = @Composable { CallerHeader(callerName, avatarName, status, callerPhotoUri) }
        val buttons = @Composable {
            if (callsState.showsActions) {
                CallActions(actions = actions, onActionClick = onActionClick)
            }
            if (!callsState.isEnded) CallControls(callsState, onAccept, onHangup, onManage)
        }

        val height = maxHeight
        if (maxWidth > height) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                    header()
                }
                ScrollingColumn(minHeight = height, modifier = Modifier.weight(1f)) { buttons() }
            }
        } else {
            ScrollingColumn(minHeight = height, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(top = Spacing.ExtraLarge)) { header() }
                buttons()
            }
        }
    }
}

@Composable
private fun ScrollingColumn(minHeight: Dp, modifier: Modifier, content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .heightIn(min = minHeight)
            .padding(vertical = Spacing.ExtraLarge),
    ) {
        content()
    }
}

@Composable
private fun CallerHeader(name: String, avatarName: String?, status: String?, photoUri: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small),
    ) {
        Avatar(name = avatarName, photoUri = photoUri, size = Sizes.AvatarLarge)
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        status?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CallControls(
    callsState: CallsState,
    onAccept: () -> Unit,
    onHangup: () -> Unit,
    onManage: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraLarge),
    ) {
        if (callsState == CallsState.ACTIVE_MULTI) {
            FloatingBubble(
                icon = Icons.Rounded.PeopleAlt,
                contentDescription = stringResource(R.string.cd_manage_call),
                onClick = onManage,
                size = Sizes.Button,
            )
        }
        FloatingBubble(
            icon = Icons.Rounded.CallEnd,
            contentDescription = stringResource(R.string.cd_hangup_call),
            onClick = onHangup,
            size = Sizes.ButtonLarge,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
        if (callsState == CallsState.INCOMING) {
            FloatingBubble(
                icon = Icons.Rounded.Call,
                contentDescription = stringResource(R.string.cd_accept_call),
                onClick = onAccept,
                size = Sizes.ButtonLarge,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

fun callStatus(elapsedSeconds: Long?, stateLabel: String?): String? =
    elapsedSeconds?.let(DateUtils::formatElapsedTime) ?: stateLabel
