package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.enum.Page
import com.chooloo.www.chooloolib.domain.enum.ThemeMode
import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakePreferenceRepository(initial: Settings = Settings()) : PreferenceRepository {
    private val state = MutableStateFlow(initial)

    override val settings: Flow<Settings> = state

    override suspend fun setDefaultPage(page: Page) = state.update { it.copy(defaultPage = page) }

    override suspend fun setThemeMode(themeMode: ThemeMode) = state.update {
        it.copy(themeMode = themeMode)
    }

    override suspend fun setIsGroupRecentsEnabled(isEnabled: Boolean) = state.update {
        it.copy(isGroupRecentsEnabled = isEnabled)
    }

    override suspend fun setIsDialpadTonesEnabled(isEnabled: Boolean) = state.update {
        it.copy(isDialpadTonesEnabled = isEnabled)
    }

    override suspend fun setIsDialpadVibrateEnabled(isEnabled: Boolean) =
        state.update { it.copy(isDialpadVibrateEnabled = isEnabled) }
}
