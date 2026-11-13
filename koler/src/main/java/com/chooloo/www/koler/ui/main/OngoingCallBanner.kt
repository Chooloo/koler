package com.chooloo.www.koler.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.koler.ui.common.CallLabels

@Composable
fun OngoingCallBanner(ongoingCall: MainViewModel.OngoingCall, onClick: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        ListItem(
            title = CallLabels.title(ongoingCall.call, LocalContext.current, ongoingCall.caller),
            subtitle = stringResource(CallLabels.labelRes(ongoingCall.call.state)),
            onClick = onClick,
            startContainer = { Icon(Icons.Rounded.Call, contentDescription = null) },
        )
    }
}
