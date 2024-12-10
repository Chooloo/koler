package com.chooloo.www.koler.ui.compose

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
fun KolerPreferences(
    defaultPage: Page?,
    isGroupRecentsEnabled: Boolean?,
    isDialpadTonesEnabled: Boolean?,
    isDialpadVibrateEnabled: Boolean?,
    incomingCallMode: IncomingCallMode?,
    onSelectedDefaultPage: (Page) -> Unit = {},
    onToggledGroupRecents: (Boolean) -> Unit = {},
    onToggledDialpadTones: (Boolean) -> Unit = {},
    onToggledDialpadVibrate: (Boolean) -> Unit = {},
    onSelectedIncomingCallMode: (IncomingCallMode) -> Unit = {}
) {
    val context = LocalContext.current

    List(
        items = ChoolooPreference.entries.toList(),
        key = { preference -> preference.key.toString() },
        item = { preference ->
            when (preference) {
                ChoolooPreference.THEME_MODE, ChoolooPreference.ANIMATIONS_ENABLED -> null

                ChoolooPreference.DEFAULT_PAGE -> ChoicePreferenceListItem(
                    values = Page.entries,
                    selectedValue = defaultPage,
                    onValueSelected = onSelectedDefaultPage,
                    subtitle = defaultPage?.name ?: "Not Selected",
                    title = context.getString(preference.titleRes),
                ) { item, selected, onClick ->
                    ListItem(title = item.name, selected = selected, onClick = onClick)
                }

                ChoolooPreference.GROUP_RECENTS_ENABLED -> SwitchPreferenceListItem(
                    value = isGroupRecentsEnabled,
                    onValueChange = onToggledGroupRecents,
                    title = context.getString(preference.titleRes),
                )

                ChoolooPreference.DIALPAD_TONES_ENABLED -> SwitchPreferenceListItem(
                    value = isDialpadTonesEnabled,
                    onValueChange = onToggledDialpadTones,
                    title = context.getString(preference.titleRes),
                )

                ChoolooPreference.INCOMING_CALL_MODE -> ChoicePreferenceListItem(
                    selectedValue = incomingCallMode,
                    values = IncomingCallMode.entries,
                    onValueSelected = onSelectedIncomingCallMode,
                    subtitle = incomingCallMode?.name ?: "Not Selected",
                    title = context.getString(preference.titleRes),
                ) { item, selected, onClick ->
                    ListItem(title = item.name, selected = selected, onClick = onClick)
                }

                ChoolooPreference.DIALPAD_VIBRATE_ENABLED -> SwitchPreferenceListItem(
                    value = isDialpadVibrateEnabled,
                    onValueChange = onToggledDialpadVibrate,
                    title = context.getString(preference.titleRes),
                )
            }
        }
    )
}