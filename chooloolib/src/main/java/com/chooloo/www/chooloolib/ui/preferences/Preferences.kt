package com.chooloo.www.chooloolib.ui.preferences

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PhoneInTalk
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.ui.common.titleRes
import com.chooloo.www.chooloolib.ui.components.list.ItemList

private enum class PreferenceSection(@StringRes val titleRes: Int) {
    APPEARANCE(R.string.pref_section_appearance),
    DIALER(R.string.pref_section_dialer),
    BLOCKING(R.string.pref_section_blocking),
    ABOUT(R.string.pref_section_about),
}

private enum class PreferenceItem(
    val section: PreferenceSection,
    val icon: ImageVector,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int? = null,
    val isDialerOnly: Boolean = false,
) {
    THEME_MODE(PreferenceSection.APPEARANCE, Icons.Rounded.Palette, R.string.pref_title_theme_mode),
    ANIMATIONS_ENABLED(PreferenceSection.APPEARANCE, Icons.Rounded.Animation, R.string.pref_title_animations),
    DEFAULT_PAGE(PreferenceSection.DIALER, Icons.Rounded.Home, R.string.pref_title_default_page, isDialerOnly = true),
    INCOMING_CALL_MODE(PreferenceSection.DIALER, Icons.Rounded.PhoneInTalk, R.string.pref_title_incoming_call_mode, isDialerOnly = true),
    GROUP_RECENTS_ENABLED(PreferenceSection.DIALER, Icons.AutoMirrored.Rounded.ViewList, R.string.pref_title_group_recents, isDialerOnly = true),
    DIALPAD_TONES_ENABLED(PreferenceSection.DIALER, Icons.Rounded.MusicNote, R.string.pref_title_dialpad_tones, isDialerOnly = true),
    DIALPAD_VIBRATE_ENABLED(PreferenceSection.DIALER, Icons.Rounded.Vibration, R.string.pref_title_dialpad_vibrate, isDialerOnly = true),
    MANAGE_BLOCKED(PreferenceSection.BLOCKING, Icons.Rounded.Block, R.string.pref_title_manage_blocked, isDialerOnly = true),
    CLEAR_RECENTS(PreferenceSection.BLOCKING, Icons.Rounded.DeleteSweep, R.string.action_clear_recents, R.string.explain_clear_recents, isDialerOnly = true),
    RATE(PreferenceSection.ABOUT, Icons.Rounded.Star, R.string.pref_title_rate),
    EMAIL(PreferenceSection.ABOUT, Icons.Rounded.Email, R.string.pref_title_email),
    REPORT_BUGS(PreferenceSection.ABOUT, Icons.Rounded.BugReport, R.string.pref_title_report_bugs),
    DONATE(PreferenceSection.ABOUT, Icons.Rounded.Favorite, R.string.pref_title_donate),
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
    showDialerPreferences: Boolean,
    onDefaultPage: (Page) -> Unit,
    onThemeMode: (ThemeMode) -> Unit,
    onIsAnimationsEnabled: (Boolean) -> Unit,
    onIsGroupRecentsEnabled: (Boolean) -> Unit,
    onIsDialpadTonesEnabled: (Boolean) -> Unit,
    onIsDialpadVibrateEnabled: (Boolean) -> Unit,
    onIncomingCallMode: (IncomingCallMode) -> Unit,
    onManageBlockedClick: () -> Unit,
    onClearRecentsClick: () -> Unit,
    onRateClick: () -> Unit,
    onEmailClick: () -> Unit,
    onReportBugsClick: () -> Unit,
    onDonateClick: () -> Unit,
) {
    val preferences = PreferenceItem.entries.filter { showDialerPreferences || !it.isDialerOnly }
    val sectionTitles = PreferenceSection.entries.associateWith { stringResource(it.titleRes) }

    ItemList(
        items = preferences,
        key = { it.name },
        header = { sectionTitles.getValue(it.section) },
        item = { preference ->
            val title = stringResource(preference.titleRes)
            val icon = preference.icon
            val description = preference.descriptionRes?.let { stringResource(it) }

            when (preference) {
                PreferenceItem.THEME_MODE -> ChoicePreferenceListItem(
                    icon = icon,
                    title = title,
                    values = ThemeMode.entries,
                    selectedValue = themeMode,
                    label = { stringResource(it.titleRes) },
                    onValueSelected = onThemeMode,
                )
                PreferenceItem.DEFAULT_PAGE -> ChoicePreferenceListItem(
                    icon = icon,
                    title = title,
                    values = Page.entries,
                    selectedValue = defaultPage,
                    label = { stringResource(it.titleRes) },
                    onValueSelected = onDefaultPage,
                )
                PreferenceItem.INCOMING_CALL_MODE -> ChoicePreferenceListItem(
                    icon = icon,
                    title = title,
                    values = IncomingCallMode.entries,
                    selectedValue = incomingCallMode,
                    label = { stringResource(it.titleRes) },
                    onValueSelected = onIncomingCallMode,
                )
                PreferenceItem.ANIMATIONS_ENABLED ->
                    SwitchPreferenceListItem(icon, title, isAnimationsEnabled, onIsAnimationsEnabled)
                PreferenceItem.GROUP_RECENTS_ENABLED ->
                    SwitchPreferenceListItem(icon, title, isGroupRecentsEnabled, onIsGroupRecentsEnabled)
                PreferenceItem.DIALPAD_TONES_ENABLED ->
                    SwitchPreferenceListItem(icon, title, isDialpadTonesEnabled, onIsDialpadTonesEnabled)
                PreferenceItem.DIALPAD_VIBRATE_ENABLED ->
                    SwitchPreferenceListItem(icon, title, isDialpadVibrateEnabled, onIsDialpadVibrateEnabled)
                PreferenceItem.MANAGE_BLOCKED -> ActionPreferenceListItem(icon, title, description, onManageBlockedClick)
                PreferenceItem.CLEAR_RECENTS -> ActionPreferenceListItem(icon, title, description, onClearRecentsClick)
                PreferenceItem.RATE -> ActionPreferenceListItem(icon, title, description, onRateClick)
                PreferenceItem.EMAIL -> ActionPreferenceListItem(icon, title, description, onEmailClick)
                PreferenceItem.REPORT_BUGS -> ActionPreferenceListItem(icon, title, description, onReportBugsClick)
                PreferenceItem.DONATE -> ActionPreferenceListItem(icon, title, description, onDonateClick)
            }
        }
    )
}
