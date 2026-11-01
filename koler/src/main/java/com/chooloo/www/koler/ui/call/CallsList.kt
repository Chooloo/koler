package com.chooloo.www.koler.ui.call

import android.telecom.Call
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallSplit
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.list.List
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.utils.LoadingState
import com.chooloo.www.koler.domain.CallData

@Composable
fun CallsList(
    items: List<CallData>,
    onItemClick: (CallData) -> Unit = {},
    onItemLongClick: (CallData) -> Unit = {},
    loadingState: LoadingState = LoadingState.IDLE,
    onHangupCallClick: (callData: CallData) -> Unit = {},
    onSeparateCallClick: (callData: CallData) -> Unit = {},
) {
    List(
        items = items,
        loadingState = loadingState,
        emptyTitle = R.string.no_results_calls,
        loadingTitle = R.string.loading_calls,
        key = { item -> item.call.details.toString() },
        emptyImage = {
            Icon(Icons.Rounded.Call, stringResource(R.string.cd_call))
        },
        item = { item ->
            ListItem(
                title = item.callerDisplayName?.takeIf { it.isNotEmpty() }
                    ?: item.number
                    ?: stringResource(R.string.caller_unknown),
                subtitle = stringResource(item.state.stringRes),
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) },
                endContainer = {
                    Row {
                        IconButton(
                            onClick = { onSeparateCallClick(item) },
                            enabled = item.isCapable(Call.Details.CAPABILITY_SEPARATE_FROM_CONFERENCE)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.CallSplit,
                                stringResource(R.string.cd_split_call)
                            )
                        }

                        IconButton(onClick = { onHangupCallClick(item) }) {
                            Icon(
                                Icons.Rounded.CallEnd,
                                stringResource(R.string.cd_hangup_call)
                            )
                        }
                    }
                }
            )
        }
    )
}
