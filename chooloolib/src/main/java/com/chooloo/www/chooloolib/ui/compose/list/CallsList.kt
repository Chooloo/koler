package com.chooloo.www.chooloolib.ui.compose.list

import android.telecom.Call
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallSplit
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.AccountData
import com.chooloo.www.chooloolib.domain.model.CallData
import com.chooloo.www.chooloolib.ui.compose.list.base.List
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem
import com.chooloo.www.chooloolib.utils.LoadingState

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
//            ioScope.launch {
//                val account = phoneUseCase.lookupAccount(item.number)
//
//                mainScope.launch {
//                    account?.photoUri?.let {
//                        setImageUri(Uri.parse(it))
//                    } ?: run {
//                        setImageResource(R.drawable.person)
//                    }
//
//                    account?.displayString?.let {
//                        titleText = it
//                        captionText = item.number
//                    } ?: run {
//                        titleText = item.number
//                    }
//                }
//            }

            ListItem(
                title = "",
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) },
                endContainer = {
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
            )
        }
    )
}
