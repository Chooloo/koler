package com.chooloo.www.chooloolib.ui.caller

import android.provider.ContactsContract.CommonDataKinds.Phone as PhoneContract
import android.text.format.DateUtils
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.components.Avatar
import com.chooloo.www.chooloolib.ui.components.Empty
import com.chooloo.www.chooloolib.ui.components.Loading
import com.chooloo.www.chooloolib.ui.components.RoundAction
import com.chooloo.www.chooloolib.ui.components.list.HeaderListItem
import com.chooloo.www.chooloolib.ui.components.list.ListItem
import com.chooloo.www.chooloolib.ui.recents.RecentTypeIcon
import com.chooloo.www.chooloolib.ui.theme.Layout
import com.chooloo.www.chooloolib.ui.theme.Sizes
import com.chooloo.www.chooloolib.ui.theme.Spacing

@Composable
fun Caller(
    state: CallerViewModel.UiState,
    onCall: (String) -> Unit,
    onMessage: (String) -> Unit,
    onWhatsapp: (String) -> Unit,
    onEmail: (String) -> Unit,
    onCopy: (String) -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleBlocked: () -> Unit,
    onEdit: (Long) -> Unit,
    onAddContact: (String?) -> Unit,
    onDelete: () -> Unit,
) {
    when (state.loadingState) {
        LoadingState.LOADING -> return Loading()
        LoadingState.FAILED -> return Empty(titleRes = R.string.error_caller_not_found)
        LoadingState.SUCCESS -> Unit
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        Header(state, onToggleFavorite, onCopy)
        Actions(
            state,
            onCall,
            onMessage,
            onWhatsapp,
            onEdit,
            onAddContact,
            onToggleBlocked,
            onDelete,
        )
        if (state.details.phones.size > 1) {
            Section(R.string.phones) {
                state.details.phones.forEach { phone -> PhoneRow(phone, onCall, onMessage, onCopy) }
            }
        }
        if (state.details.emails.isNotEmpty()) {
            Section(R.string.emails) {
                state.details.emails.forEach { email ->
                    ListItem(title = email, onClick = { onEmail(email) })
                }
            }
        }
        if (state.history.isNotEmpty()) {
            History(state.history, onCall, onCopy)
        }
    }
}

@Composable
private fun Header(
    state: CallerViewModel.UiState,
    onToggleFavorite: () -> Unit,
    onCopy: (String) -> Unit,
) {
    val title = state.name ?: state.number ?: stringResource(R.string.caller_unknown)

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Avatar(name = state.name, photoUri = state.contact?.photoUri, size = Sizes.AvatarLarge)
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
            state.details.organization?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (state.name != null && state.hasNumber) {
                state.number?.let { NumberChip(it, state.details.phones, onCopy) }
            }
        }
        state.contact?.let { contact ->
            IconButton(onClick = onToggleFavorite, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(
                    imageVector =
                        if (contact.isFavorite) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                    contentDescription = stringResource(R.string.cd_favorite_star),
                    tint = if (contact.isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
    }
}

@Composable
private fun NumberChip(number: String, phones: List<Phone>, onCopy: (String) -> Unit) {
    val resources = LocalResources.current
    val typeLabel = phones.firstOrNull { it.number == number }
        ?.let { PhoneContract.getTypeLabel(resources, it.type, it.label).toString() }
    AssistChip(
        onClick = { onCopy(number) },
        label = { Text(listOfNotNull(number, typeLabel).joinToString(" · ")) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.ContentCopy,
                contentDescription = stringResource(R.string.cd_copy_number),
                modifier = Modifier.size(AssistChipDefaults.IconSize),
            )
        },
    )
}

private class CallerAction(
    val icon: ImageVector,
    val label: String,
    val isPrimary: Boolean = false,
    val onClick: () -> Unit,
)

@Composable
private fun Actions(
    state: CallerViewModel.UiState,
    onCall: (String) -> Unit,
    onMessage: (String) -> Unit,
    onWhatsapp: (String) -> Unit,
    onEdit: (Long) -> Unit,
    onAddContact: (String?) -> Unit,
    onToggleBlocked: () -> Unit,
    onDelete: () -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val number = state.number?.takeIf { state.hasNumber }
    val contact = state.contact

    val primary = buildList {
        number?.let {
            add(
                CallerAction(
                    Icons.Rounded.Call,
                    stringResource(R.string.action_call),
                    isPrimary = true,
                ) {
                    onCall(it)
                },
            )
            add(
                CallerAction(
                    Icons.AutoMirrored.Rounded.Message,
                    stringResource(R.string.action_message),
                ) {
                    onMessage(it)
                },
            )
        }
        state.details.whatsappNumber?.let {
            add(
                CallerAction(Icons.Rounded.Chat, stringResource(R.string.type_label_whatsapp)) {
                    onWhatsapp(it)
                },
            )
        }
        if (contact != null) {
            add(
                CallerAction(Icons.Rounded.Edit, stringResource(R.string.action_edit)) {
                    onEdit(contact.id)
                },
            )
        } else {
            add(
                CallerAction(Icons.Rounded.PersonAdd, stringResource(R.string.add_contact)) {
                    onAddContact(number)
                },
            )
        }
    }
    val extra = buildList {
        state.isBlocked?.let { isBlocked ->
            add(
                CallerAction(
                    Icons.Rounded.Block,
                    stringResource(
                        if (isBlocked) R.string.action_unblock else R.string.action_block,
                    ),
                    onClick = onToggleBlocked,
                ),
            )
        }
        if (contact != null) {
            add(
                CallerAction(
                    Icons.Rounded.DeleteOutline,
                    stringResource(R.string.action_delete),
                    onClick = onDelete,
                ),
            )
        }
    }
    val toggle = CallerAction(
        icon = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.MoreHoriz,
        label = stringResource(if (isExpanded) R.string.action_less else R.string.action_more),
    ) { isExpanded = !isExpanded }
    val visible = when {
        extra.isEmpty() -> primary
        isExpanded -> primary + extra + toggle
        else -> primary + toggle
    }

    FlowRow(
        maxItemsInEachRow = Layout.CALLER_ACTIONS_PER_ROW,
        horizontalArrangement = Arrangement.spacedBy(Spacing.Large, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        visible.forEach {
            RoundAction(
                icon = it.icon,
                label = it.label,
                onClick = it.onClick,
                isHighlighted = it.isPrimary,
            )
        }
    }
}

@Composable
private fun PhoneRow(
    phone: Phone,
    onCall: (String) -> Unit,
    onMessage: (String) -> Unit,
    onCopy: (String) -> Unit,
) {
    val resources = LocalResources.current
    ListItem(
        title = phone.number,
        subtitle = PhoneContract.getTypeLabel(resources, phone.type, phone.label).toString(),
        onClick = { onCall(phone.number) },
        onLongClick = { onCopy(phone.number) },
        endContainer = {
            IconButton(onClick = { onMessage(phone.number) }) {
                Icon(Icons.AutoMirrored.Rounded.Message, stringResource(R.string.action_message))
            }
        },
    )
}

@Composable
private fun History(history: List<Recent>, onCall: (String) -> Unit, onCopy: (String) -> Unit) {
    val context = LocalContext.current
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val visible = if (isExpanded) history else history.take(Layout.COLLAPSED_HISTORY_SIZE)

    Section(R.string.recent_calls) {
        visible.forEach { recent ->
            val time = DateUtils.formatDateTime(
                context,
                recent.dateMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or
                    DateUtils.FORMAT_ABBREV_MONTH,
            )
            val duration =
                recent.durationSeconds.takeIf { it > 0 }?.let(DateUtils::formatElapsedTime)
            ListItem(
                title = listOfNotNull(time, duration).joinToString(" · "),
                onClick = { onCall(recent.number) },
                onLongClick = { onCopy(recent.number) },
                endContainer = {
                    RecentTypeIcon(type = recent.type, contentDescription = null)
                },
            )
        }
        if (history.size > Layout.COLLAPSED_HISTORY_SIZE) {
            TextButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.padding(horizontal = Spacing.Small),
            ) {
                Text(stringResource(if (isExpanded) R.string.show_less else R.string.show_all))
            }
        }
    }
}

@Composable
private fun Section(@StringRes titleRes: Int, content: @Composable () -> Unit) {
    Column {
        HeaderListItem(header = stringResource(titleRes))
        content()
    }
}
