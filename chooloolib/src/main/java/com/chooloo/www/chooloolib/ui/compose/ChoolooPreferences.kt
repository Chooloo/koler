package com.chooloo.www.chooloolib.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.ChoolooPreference
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.Page
import com.chooloo.www.chooloolib.ui.compose.list.base.List
import com.chooloo.www.chooloolib.ui.compose.list.item.ListItem
import com.chooloo.www.chooloolib.ui.compose.list.item.preference.ChoicePreferenceListItem
import com.chooloo.www.chooloolib.ui.compose.list.item.preference.SwitchPreferenceListItem
import com.chooloo.www.chooloolib.utils.beautify

@Composable
fun ChoolooPreferences(
    defaultPage: Page? = null,
    themeMode: ThemeMode? = null,
    isAnimationsEnabled: Boolean? = null,
    isGroupRecentsEnabled: Boolean? = null,
    isDialpadTonesEnabled: Boolean? = null,
    isDialpadVibrateEnabled: Boolean? = null,
    incomingCallMode: IncomingCallMode? = null,
    showDialerPreferences: Boolean = true,
    onDefaultPage: (Page) -> Unit = {},
    onThemeMode: (ThemeMode) -> Unit = {},
    onIsAnimationsEnabled: (Boolean) -> Unit = {},
    onIsGroupRecentsEnabled: (Boolean) -> Unit = {},
    onIsDialpadTonesEnabled: (Boolean) -> Unit = {},
    onIsDialpadVibrateEnabled: (Boolean) -> Unit = {},
    onIncomingCallMode: (IncomingCallMode) -> Unit = {},
    onManageBlockedClick: () -> Unit = {},
    onClearRecentsClick: () -> Unit = {},
    onRateClick: () -> Unit = {},
    onEmailClick: () -> Unit = {},
    onReportBugsClick: () -> Unit = {},
    onDonateClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val preferences = ChoolooPreference.entries.filter { showDialerPreferences || !it.isDialerOnly }

    List(
        items = preferences,
        key = { preference -> preference.key.toString() },
        item = { preference ->
            val title = context.getString(preference.titleRes)
            val description = preference.descriptionRes?.let(context::getString)

            when (preference) {
                ChoolooPreference.THEME_MODE -> ChoicePreferenceListItem(
                    values = ThemeMode.entries,
                    selectedValue = themeMode,
                    onValueSelected = onThemeMode,
                    subtitle = themeMode?.let { stringResource(it.titleRes) } ?: "Not Selected",
                    title = title,
                ) { item, selected, onClick ->
                    ListItem(
                        title = stringResource(item.titleRes),
                        selected = selected,
                        onClick = onClick
                    )
                }

                ChoolooPreference.ANIMATIONS_ENABLED -> SwitchPreferenceListItem(
                    value = isAnimationsEnabled,
                    onValueChange = onIsAnimationsEnabled,
                    title = title,
                )

                ChoolooPreference.DEFAULT_PAGE -> ChoicePreferenceListItem(
                    selectedValue = defaultPage,
                    values = Page.entries,
                    onValueSelected = onDefaultPage,
                    subtitle = defaultPage?.name?.beautify() ?: "Not Selected",
                    title = title,
                ) { item, selected, onClick ->
                    ListItem(title = item.name.beautify(), onClick = onClick, selected = selected)
                }

                ChoolooPreference.GROUP_RECENTS_ENABLED -> SwitchPreferenceListItem(
                    value = isGroupRecentsEnabled,
                    onValueChange = onIsGroupRecentsEnabled,
                    title = title,
                )

                ChoolooPreference.DIALPAD_TONES_ENABLED -> SwitchPreferenceListItem(
                    value = isDialpadTonesEnabled,
                    onValueChange = onIsDialpadTonesEnabled,
                    title = title,
                )

                ChoolooPreference.INCOMING_CALL_MODE -> ChoicePreferenceListItem(
                    selectedValue = incomingCallMode,
                    onValueSelected = onIncomingCallMode,
                    values = IncomingCallMode.entries,
                    subtitle = incomingCallMode?.name?.beautify() ?: "Not Selected",
                    title = title,
                ) { item, selected, onClick ->
                    ListItem(title = item.name.beautify(), selected = selected, onClick = onClick)
                }

                ChoolooPreference.DIALPAD_VIBRATE_ENABLED -> SwitchPreferenceListItem(
                    value = isDialpadVibrateEnabled,
                    onValueChange = onIsDialpadVibrateEnabled,
                    title = title,
                )

                ChoolooPreference.MANAGE_BLOCKED -> ListItem(
                    title = title,
                    subtitle = description,
                    onClick = onManageBlockedClick
                )

                ChoolooPreference.CLEAR_RECENTS -> ListItem(
                    title = title,
                    subtitle = description,
                    onClick = onClearRecentsClick
                )

                ChoolooPreference.RATE -> ListItem(
                    title = title,
                    subtitle = description,
                    onClick = onRateClick
                )

                ChoolooPreference.EMAIL -> ListItem(
                    title = title,
                    subtitle = description,
                    onClick = onEmailClick
                )

                ChoolooPreference.REPORT_BUGS -> ListItem(
                    title = title,
                    subtitle = description,
                    onClick = onReportBugsClick
                )

                ChoolooPreference.DONATE -> ListItem(
                    title = title,
                    subtitle = description,
                    onClick = onDonateClick
                )
            }
        }
    )
}
