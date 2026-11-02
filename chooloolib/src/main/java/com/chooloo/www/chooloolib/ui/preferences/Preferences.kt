package com.chooloo.www.chooloolib.ui.preferences

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.ui.common.titleRes
import com.chooloo.www.chooloolib.ui.components.list.List
import com.chooloo.www.chooloolib.ui.components.list.ListItem

private enum class PreferenceItem(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int? = null,
    val isDialerOnly: Boolean = false,
) {
    THEME_MODE(R.string.pref_title_theme_mode),
    ANIMATIONS_ENABLED(R.string.pref_title_animations),
    DEFAULT_PAGE(R.string.pref_title_default_page, isDialerOnly = true),
    INCOMING_CALL_MODE(R.string.pref_title_incoming_call_mode, isDialerOnly = true),
    GROUP_RECENTS_ENABLED(R.string.pref_title_group_recents, isDialerOnly = true),
    DIALPAD_TONES_ENABLED(R.string.pref_title_dialpad_tones, isDialerOnly = true),
    DIALPAD_VIBRATE_ENABLED(R.string.pref_title_dialpad_vibrate, isDialerOnly = true),
    MANAGE_BLOCKED(R.string.pref_title_manage_blocked, isDialerOnly = true),
    CLEAR_RECENTS(R.string.action_clear_recents, R.string.explain_clear_recents, isDialerOnly = true),
    RATE(R.string.pref_title_rate),
    EMAIL(R.string.pref_title_email),
    REPORT_BUGS(R.string.pref_title_report_bugs),
    DONATE(R.string.pref_title_donate),
}

@Composable
fun Preferences(
    defaultPage: Page,
    themeMode: ThemeMode,
    isAnimationsEnabled: Boolean,
    isGroupRecentsEnabled: Boolean,
    isDialpadTonesEnabled: Boolean,
    isDialpadVibrateEnabled: Boolean,
    incomingCallMode: IncomingCallMode,
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
    val preferences = PreferenceItem.entries.filter { showDialerPreferences || !it.isDialerOnly }

    List(
        items = preferences,
        key = { it.name },
        item = { preference ->
            val title = stringResource(preference.titleRes)
            val description = preference.descriptionRes?.let { stringResource(it) }

            when (preference) {
                PreferenceItem.THEME_MODE -> ChoicePreferenceListItem(
                    title = title,
                    values = ThemeMode.entries,
                    selectedValue = themeMode,
                    subtitle = stringResource(themeMode.titleRes),
                    onValueSelected = onThemeMode,
                ) { item, selected, onClick ->
                    ListItem(title = stringResource(item.titleRes), selected = selected, onClick = onClick)
                }

                PreferenceItem.DEFAULT_PAGE -> ChoicePreferenceListItem(
                    title = title,
                    values = Page.entries,
                    selectedValue = defaultPage,
                    subtitle = stringResource(defaultPage.titleRes),
                    onValueSelected = onDefaultPage,
                ) { item, selected, onClick ->
                    ListItem(title = stringResource(item.titleRes), selected = selected, onClick = onClick)
                }

                PreferenceItem.INCOMING_CALL_MODE -> ChoicePreferenceListItem(
                    title = title,
                    values = IncomingCallMode.entries,
                    selectedValue = incomingCallMode,
                    subtitle = stringResource(incomingCallMode.titleRes),
                    onValueSelected = onIncomingCallMode,
                ) { item, selected, onClick ->
                    ListItem(title = stringResource(item.titleRes), selected = selected, onClick = onClick)
                }

                PreferenceItem.ANIMATIONS_ENABLED ->
                    SwitchPreferenceListItem(title = title, value = isAnimationsEnabled, onValueChange = onIsAnimationsEnabled)

                PreferenceItem.GROUP_RECENTS_ENABLED ->
                    SwitchPreferenceListItem(title = title, value = isGroupRecentsEnabled, onValueChange = onIsGroupRecentsEnabled)

                PreferenceItem.DIALPAD_TONES_ENABLED ->
                    SwitchPreferenceListItem(title = title, value = isDialpadTonesEnabled, onValueChange = onIsDialpadTonesEnabled)

                PreferenceItem.DIALPAD_VIBRATE_ENABLED ->
                    SwitchPreferenceListItem(title = title, value = isDialpadVibrateEnabled, onValueChange = onIsDialpadVibrateEnabled)

                PreferenceItem.MANAGE_BLOCKED -> ListItem(title = title, subtitle = description, onClick = onManageBlockedClick)
                PreferenceItem.CLEAR_RECENTS -> ListItem(title = title, subtitle = description, onClick = onClearRecentsClick)
                PreferenceItem.RATE -> ListItem(title = title, subtitle = description, onClick = onRateClick)
                PreferenceItem.EMAIL -> ListItem(title = title, subtitle = description, onClick = onEmailClick)
                PreferenceItem.REPORT_BUGS -> ListItem(title = title, subtitle = description, onClick = onReportBugsClick)
                PreferenceItem.DONATE -> ListItem(title = title, subtitle = description, onClick = onDonateClick)
            }
        }
    )
}
