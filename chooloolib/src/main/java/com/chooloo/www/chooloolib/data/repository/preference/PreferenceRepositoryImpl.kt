package com.chooloo.www.chooloolib.data.repository.preference

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.StringRes
import androidx.core.content.edit
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.di.IoScope
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.Page
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context
) : PreferenceRepository {
    override val isAnimationsEnabled: StateFlow<Boolean> =
        getBooleanPreferenceFlow(
            keyRes = R.string.pref_key_animations,
            defaultValue = true
        )

    override val isGroupRecentsEnabled: StateFlow<Boolean> =
        getBooleanPreferenceFlow(
            keyRes = R.string.pref_key_group_recents,
            defaultValue = false
        )

    override val isDialpadTonesEnabled: StateFlow<Boolean> =
        getBooleanPreferenceFlow(
            keyRes = R.string.pref_key_dialpad_tones,
            defaultValue = true
        )

    override val isDialpadVibrateEnabled: StateFlow<Boolean> =
        getBooleanPreferenceFlow(
            keyRes = R.string.pref_key_dialpad_vibrate,
            defaultValue = true
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
        putString(R.string.pref_key_default_page, page.key)
    }

    override fun setThemeMode(themeMode: ThemeMode) {
        putString(R.string.pref_key_theme_mode, themeMode.key)
    }

    override fun setIsAnimationsEnabled(isEnabled: Boolean) {
        putBoolean(R.string.pref_key_animations, isEnabled)
    }

    override fun setIsGroupRecentsEnabled(isEnabled: Boolean) {
        putBoolean(R.string.pref_key_group_recents, isEnabled)
    }

    override fun setIsDialpadTonesEnabled(isEnabled: Boolean) {
        putBoolean(R.string.pref_key_dialpad_tones, isEnabled)
    }

    override fun setIsDialpadVibrateEnabled(isEnabled: Boolean) {
        putBoolean(R.string.pref_key_dialpad_vibrate, isEnabled)
    }

    override fun setIncomingCallModel(incomingCallMode: IncomingCallMode) {
        putString(R.string.pref_key_incoming_call_mode, incomingCallMode.key)
    }

    private fun getBoolean(@StringRes keyRes: Int, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(
            context.getString(keyRes),
            defaultValue
        )

    private fun putBoolean(@StringRes keyRes: Int, value: Boolean) {
        sharedPreferences.edit { putBoolean(context.getString(keyRes), value) }
    }

    private fun getString(@StringRes keyRes: Int, defaultValue: String? = null): String? =
        sharedPreferences.getString(context.getString(keyRes), defaultValue)

    private fun putString(@StringRes keyRes: Int, value: String?) {
        sharedPreferences.edit { putString(context.getString(keyRes), value) }
    }

    private fun getBooleanPreferenceFlow(
        defaultValue: Boolean,
        @StringRes keyRes: Int
    ): StateFlow<Boolean> {
        val prefKey = context.getString(keyRes)
        return callbackFlow {
            val listener = OnSharedPreferenceChangeListener { _, key ->
                if (key == prefKey) {
                    trySend(getBoolean(keyRes, defaultValue))
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.stateIn(
            ioScope,
            SharingStarted.Eagerly,
            getBoolean(keyRes, defaultValue)
        )
    }

    private fun <ItemType> getDataPreferenceFlow(
        @StringRes keyRes: Int,
        converter: (String?) -> ItemType
    ): StateFlow<ItemType> {
        val prefKey = context.getString(keyRes)
        return callbackFlow {
            val listener = OnSharedPreferenceChangeListener { _, key ->
                if (key == prefKey) {
                    trySend(converter(getString(keyRes)))
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.stateIn(
            ioScope,
            SharingStarted.Eagerly,
            converter(getString(keyRes))
        )
    }
}
