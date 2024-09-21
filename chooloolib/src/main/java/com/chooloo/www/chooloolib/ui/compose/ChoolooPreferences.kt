package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.ChoolooPreference
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.Page
import com.chooloo.www.chooloolib.ui.compose.list.base.List
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem
import com.chooloo.www.chooloolib.ui.compose.list.item.preference.ChoicePreferenceListItem
import com.chooloo.www.chooloolib.ui.compose.list.item.preference.SwitchPreferenceListItem


@Composable
fun ChoolooPreferences(
    defaultPage: Page?,
    isGroupRecentsEnabled: Boolean?,
    isDialpadTonesEnabled: Boolean?,
    isDialpadVibrateEnabled: Boolean?,
    incomingCallMode: IncomingCallMode?,
    onDefaultPage: (Page) -> Unit,
    onIsGroupRecentsEnabled: (Boolean) -> Unit,
    onIsDialpadTonesEnabled: (Boolean) -> Unit,
    onIsDialpadVibrateEnabled: (Boolean) -> Unit,
    onIncomingCallMode: (IncomingCallMode) -> Unit
) {
    val context = LocalContext.current
    List(
        items = ChoolooPreference.entries,
        item = { preference ->
            when (preference) {
                ChoolooPreference.THEME_MODE, ChoolooPreference.ANIMATIONS_ENABLED -> null

                ChoolooPreference.DEFAULT_PAGE -> ChoicePreferenceListItem(
                    selectedValue = defaultPage,
                    values = Page.entries,
                    onValueSelected = onDefaultPage,
                    title = context.getString(preference.titleRes),
                ) { item, selected, onClick ->
                    ListItem(title = item.name, onClick = onClick, selected = selected)
                }

                ChoolooPreference.GROUP_RECENTS_ENABLED -> SwitchPreferenceListItem(
                    value = isGroupRecentsEnabled,
                    onValueChange = onIsGroupRecentsEnabled,
                    title = context.getString(preference.titleRes),
                )

                ChoolooPreference.DIALPAD_TONES_ENABLED -> SwitchPreferenceListItem(
                    value = isDialpadTonesEnabled,
                    onValueChange = onIsDialpadTonesEnabled,
                    title = context.getString(preference.titleRes),
                )

                ChoolooPreference.INCOMING_CALL_MODE -> ChoicePreferenceListItem(
                    selectedValue = incomingCallMode,
                    onValueSelected = onIncomingCallMode,
                    values = IncomingCallMode.entries,
                    title = context.getString(preference.titleRes),
                ) { item, selected, onClick ->
                    ListItem(title = item.name, selected = selected, onClick = onClick)
                }

                ChoolooPreference.DIALPAD_VIBRATE_ENABLED -> SwitchPreferenceListItem(
                    value = isDialpadVibrateEnabled,
                    onValueChange = onIsDialpadVibrateEnabled,
                    title = context.getString(preference.titleRes),
                )
            }
        })
}