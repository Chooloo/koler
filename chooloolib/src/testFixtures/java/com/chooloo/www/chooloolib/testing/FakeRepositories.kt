package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.domain.repository.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class FakeAudioRepository : AudioRepository {
    val playedTones = mutableListOf<Char>()
    var vibrations = 0

    override suspend fun playDtmfTone(key: Char) {
        playedTones += key
    }

    override suspend fun vibrate() {
        vibrations++
    }
}

class FakeTelecomRepository(private val defaultDialer: Boolean = true) : TelecomRepository {
    val calledNumbers = mutableListOf<String>()
    val secretCodes = mutableListOf<String>()
    var voicemailCalls = 0
    var callError: Exception? = null
    var secretCodeError: Exception? = null

    override fun isDefaultDialer() = defaultDialer

    override suspend fun callNumber(number: String) {
        callError?.let { throw it }
        calledNumbers += number
    }

    override suspend fun callVoicemail() {
        voicemailCalls++
    }

    override suspend fun handleSecretCode(code: String): Boolean {
        secretCodeError?.let { throw it }
        secretCodes += code
        return false
    }
}

class FakePhoneRepository(private val lookups: Map<String, PhoneLookup> = emptyMap()) : PhoneRepository {
    override fun phones(contactId: Long?, filter: String?): Flow<List<Phone>> = flowOf(emptyList())

    override suspend fun lookup(number: String): PhoneLookup? = lookups[number]
}

class FakeRecentRepository(var lastOutgoingNumber: String? = null) : RecentRepository {
    override fun recents(filter: String?): Flow<List<Recent>> = flowOf(emptyList())

    override fun recent(recentId: Long): Flow<Recent?> = flowOf(null)

    override fun recentsOfNumbers(numbers: List<String>): Flow<List<Recent>> = flowOf(emptyList())

    override suspend fun lastOutgoingNumber() = lastOutgoingNumber

    override suspend fun deleteRecent(recentId: Long) = Unit

    override suspend fun deleteAllRecents() = Unit
}

class FakePreferenceRepository(initial: Settings = Settings()) : PreferenceRepository {
    private val state = MutableStateFlow(initial)

    override val settings: Flow<Settings> = state

    override suspend fun setDefaultPage(page: Page) = state.update { it.copy(defaultPage = page) }

    override suspend fun setThemeMode(themeMode: ThemeMode) = state.update { it.copy(themeMode = themeMode) }

    override suspend fun setIsAnimationsEnabled(isEnabled: Boolean) = state.update { it.copy(isAnimationsEnabled = isEnabled) }

    override suspend fun setIsGroupRecentsEnabled(isEnabled: Boolean) = state.update { it.copy(isGroupRecentsEnabled = isEnabled) }

    override suspend fun setIsDialpadTonesEnabled(isEnabled: Boolean) = state.update { it.copy(isDialpadTonesEnabled = isEnabled) }

    override suspend fun setIsDialpadVibrateEnabled(isEnabled: Boolean) =
        state.update { it.copy(isDialpadVibrateEnabled = isEnabled) }
}
