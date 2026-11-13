package com.chooloo.www.koler.ui.call

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CallSplit
import androidx.compose.material.icons.rounded.Call as CallIcon
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.components.list.ItemList
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.koler.domain.Call
import com.chooloo.www.koler.ui.common.labelRes
import com.chooloo.www.koler.ui.common.title

@Composable
fun CallsList(
    items: List<Call>,
    onHangupCallClick: (Call) -> Unit,
    onSeparateCallClick: (Call) -> Unit,
) {
    ItemList(
        items = items,
        emptyTitle = R.string.no_results_calls,
        key = { item -> item.id },
        emptyIcon = Icons.Rounded.CallIcon,
        item = { item ->
            ListItem(
                title = item.title(LocalContext.current, null),
                subtitle = stringResource(item.state.labelRes),
                endContainer = {
                    Row {
                        IconButton(
                            onClick = { onSeparateCallClick(item) },
                            enabled = item.can(Call.Capability.SEPARATE)
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
