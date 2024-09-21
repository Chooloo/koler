package com.chooloo.www.chooloolib.domain.repository.preference

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.StringRes
import androidx.preference.PreferenceManager
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.di.module.IoScope
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.Page
import com.chooloo.www.chooloolib.domain.repository.theme.ThemeRepository.ThemeMode
import com.chooloo.www.chooloolib.utils.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    private val prefs: PreferencesManager,
    @IoScope private val ioScope: CoroutineScope,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : BaseRepositoryImpl(), PreferenceRepository {
    override val isAnimationsEnabled: StateFlow<Boolean> =
        getBooleanPreferenceFlow(
            keyRes = R.string.pref_key_animations,
            defaultKeyRes = R.bool.pref_default_value_animations
        )

    override val isGroupRecentsEnabled: StateFlow<Boolean> =
        getBooleanPreferenceFlow(
            keyRes = R.string.pref_key_group_recents,
            defaultKeyRes = R.bool.pref_default_value_group_recents
        )

    override val isDialpadTonesEnabled: StateFlow<Boolean> =
        getBooleanPreferenceFlow(
            keyRes = R.string.pref_key_dialpad_tones,
            defaultKeyRes = R.bool.pref_default_value_dialpad_tones
        )

    override val isDialpadVibrateEnabled: StateFlow<Boolean> =
        getBooleanPreferenceFlow(
            keyRes = R.string.pref_key_dialpad_vibrate,
            defaultKeyRes = R.bool.pref_default_value_dialpad_vibrate
        )

    override val defaultPage: StateFlow<Page> =
        getDataPreferenceFlow(R.string.pref_key_default_page, Page.Companion::fromKey)

    override val themeMode: StateFlow<ThemeMode> =
        getDataPreferenceFlow(R.string.pref_key_theme_mode, ThemeMode.Companion::fromKey)

    override val incomingCallMode: StateFlow<IncomingCallMode> =
        getDataPreferenceFlow(
            R.string.pref_key_incoming_call_mode,
            IncomingCallMode.Companion::fromKey
        )

    override fun setDefaultPage(page: Page) {
        prefs.putString(R.string.pref_key_default_page, page.key)
    }

    override fun setThemeMode(themeMode: ThemeMode) {
        prefs.putString(R.string.pref_key_theme_mode, themeMode.key)
    }

    override fun setIsAnimationsEnabled(isEnabled: Boolean) {
        prefs.putBoolean(R.string.pref_key_animations, isEnabled)
    }

    override fun setIsGroupRecentsEnabled(isEnabled: Boolean) {
        prefs.putBoolean(R.string.pref_key_group_recents, isEnabled)
    }

    override fun setIsDialpadTonesEnabled(isEnabled: Boolean) {
        prefs.putBoolean(R.string.pref_key_dialpad_tones, isEnabled)
    }

    override fun setIsDialpadVibrateEnabled(isEnabled: Boolean) {
        prefs.putBoolean(R.string.pref_key_dialpad_vibrate, isEnabled)
    }

    override fun setIncomingCallModel(incomingCallMode: IncomingCallMode) {
        prefs.putString(R.string.pref_key_incoming_call_mode, incomingCallMode.key)
    }

    private fun getBooleanPreferenceFlow(
        defaultKeyRes: Int,
        @StringRes keyRes: Int
    ): StateFlow<Boolean> =
        callbackFlow {
            val listener = OnSharedPreferenceChangeListener { _, key ->
                if (key == keyRes.toString()) {
                    trySend(prefs.getBoolean(keyRes, defaultKeyRes))
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.stateIn(
            ioScope,
            SharingStarted.WhileSubscribed(),
            prefs.getBoolean(keyRes, defaultKeyRes)
        )

    private fun getStringPreferenceFlow(
        @StringRes keyRes: Int,
        defaultValue: String? = null
    ): Flow<String?> =
        callbackFlow {
            val listener = OnSharedPreferenceChangeListener { _, key ->
                if (key == keyRes.toString()) {
                    trySend(prefs.getString(keyRes, defaultValue))
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.stateIn(
            ioScope,
            SharingStarted.WhileSubscribed(),
            prefs.getString(keyRes, defaultValue)
        )

    private fun <ItemType> getDataPreferenceFlow(
        @StringRes keyRes: Int,
        converter: (String?) -> ItemType
    ): StateFlow<ItemType> =
        callbackFlow {
            val listener = OnSharedPreferenceChangeListener { _, key ->
                if (key == keyRes.toString()) {
                    trySend(converter(prefs.getString(keyRes)))
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.stateIn(
            ioScope,
            SharingStarted.WhileSubscribed(),
            converter(prefs.getString(keyRes))
        )
}