package com.chooloo.www.chooloolib.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.chooloo.www.chooloolib.di.IoScope
import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    @IoScope private val ioScope: CoroutineScope,
    private val sharedPreferences: SharedPreferences,
) : PreferenceRepository {
    override val defaultPage = enumPreference(KEY_DEFAULT_PAGE, PAGE_KEYS, Page.CONTACTS)
    override val themeMode = enumPreference(KEY_THEME_MODE, THEME_MODE_KEYS, ThemeMode.SYSTEM)
    override val isAnimationsEnabled = booleanPreference(KEY_ANIMATIONS, true)
    override val isGroupRecentsEnabled = booleanPreference(KEY_GROUP_RECENTS, false)
    override val isDialpadTonesEnabled = booleanPreference(KEY_DIALPAD_TONES, true)
    override val isDialpadVibrateEnabled = booleanPreference(KEY_DIALPAD_VIBRATE, true)
    override val incomingCallMode = enumPreference(KEY_INCOMING_CALL_MODE, INCOMING_CALL_MODE_KEYS, IncomingCallMode.FULL_SCREEN)

    override fun setDefaultPage(page: Page) = putString(KEY_DEFAULT_PAGE, PAGE_KEYS.getValue(page))
    override fun setThemeMode(themeMode: ThemeMode) = putString(KEY_THEME_MODE, THEME_MODE_KEYS.getValue(themeMode))
    override fun setIsAnimationsEnabled(isEnabled: Boolean) = putBoolean(KEY_ANIMATIONS, isEnabled)
    override fun setIsGroupRecentsEnabled(isEnabled: Boolean) = putBoolean(KEY_GROUP_RECENTS, isEnabled)
    override fun setIsDialpadTonesEnabled(isEnabled: Boolean) = putBoolean(KEY_DIALPAD_TONES, isEnabled)
    override fun setIsDialpadVibrateEnabled(isEnabled: Boolean) = putBoolean(KEY_DIALPAD_VIBRATE, isEnabled)
    override fun setIncomingCallMode(incomingCallMode: IncomingCallMode) =
        putString(KEY_INCOMING_CALL_MODE, INCOMING_CALL_MODE_KEYS.getValue(incomingCallMode))

    private fun putString(key: String, value: String) = sharedPreferences.edit { putString(key, value) }

    private fun putBoolean(key: String, value: Boolean) = sharedPreferences.edit { putBoolean(key, value) }

    private fun booleanPreference(key: String, default: Boolean): StateFlow<Boolean> =
        preference(key) { sharedPreferences.getBoolean(key, default) }

    private fun <T : Enum<T>> enumPreference(key: String, keys: Map<T, String>, default: T): StateFlow<T> =
        preference(key) {
            val stored = sharedPreferences.getString(key, null)
            keys.entries.firstOrNull { it.value == stored }?.key ?: default
        }

    private fun <T> preference(key: String, read: () -> T): StateFlow<T> =
        callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
                if (changedKey == key) trySend(read())
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
        }.stateIn(ioScope, SharingStarted.Eagerly, read())

    private companion object {
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_ANIMATIONS = "animations"
        const val KEY_DEFAULT_PAGE = "default_page"
        const val KEY_GROUP_RECENTS = "group_recents"
        const val KEY_DIALPAD_TONES = "dialpad_tones"
        const val KEY_DIALPAD_VIBRATE = "dialpad_vibrate"
        const val KEY_INCOMING_CALL_MODE = "incoming_call_mode"

        val PAGE_KEYS = mapOf(Page.DIALER to "dialer", Page.CONTACTS to "contacts", Page.RECENTS to "recents")
        val THEME_MODE_KEYS = mapOf(
            ThemeMode.SYSTEM to "system",
            ThemeMode.LIGHT to "light",
            ThemeMode.DARK to "dark",
            ThemeMode.DYNAMIC to "dynamic",
        )
        val INCOMING_CALL_MODE_KEYS = mapOf(
            IncomingCallMode.FULL_SCREEN to "full_screen",
            IncomingCallMode.POP_UP to "popup_notification",
        )
    }
}
