package com.chooloo.www.koler.ui.main

import androidx.lifecycle.SavedStateHandle
import com.chooloo.www.chooloolib.domain.enum.Page
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.testing.FakePhoneRepository
import com.chooloo.www.chooloolib.testing.FakePreferenceRepository
import com.chooloo.www.chooloolib.testing.FakeRecentRepository
import com.chooloo.www.chooloolib.testing.FakeTelecomRepository
import com.chooloo.www.chooloolib.testing.MainDispatcherRule
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call
import com.chooloo.www.koler.domain.usecase.ClearMissedCalls
import com.chooloo.www.koler.domain.usecase.GetPrimaryCall
import com.chooloo.www.koler.ui.call.FakeCallRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val callRepository = FakeCallRepository()
    private val telecomRepository = FakeTelecomRepository()
    private val recentRepository = FakeRecentRepository()
    private val lookup = PhoneLookup(contactId = 1, name = "Dana")
    private val viewModel by lazy {
        MainViewModel(
            SavedStateHandle(),
            FakePreferenceRepository(),
            callRepository,
            ClearMissedCalls(telecomRepository, recentRepository),
            FakePhoneRepository(mapOf("111" to lookup)),
            GetPrimaryCall(),
        )
    }

    private fun runSubscribed(block: suspend () -> Unit) = runTest(mainDispatcherRule.dispatcher) {
        viewModel.ongoingCall.launchIn(backgroundScope)
        viewModel.uiState.launchIn(backgroundScope)
        block()
    }

    @Test
    fun `ongoing call follows the primary call and its caller`() = runSubscribed {
        val held = Call(id = "c1", state = CallState.HOLDING, number = "222")
        val active = Call(id = "c2", state = CallState.ACTIVE, number = "111")

        callRepository.calls.value = listOf(held, active)

        assertEquals(MainViewModel.OngoingCall(active, lookup), viewModel.ongoingCall.value)
    }

    @Test
    fun `ongoing call without a number has no caller`() = runSubscribed {
        val call = Call(id = "c1", state = CallState.ACTIVE, number = null)

        callRepository.calls.value = listOf(call)

        assertEquals(MainViewModel.OngoingCall(call, null), viewModel.ongoingCall.value)
    }

    @Test
    fun `ongoing call clears when the last call ends`() = runSubscribed {
        callRepository.calls.value =
            listOf(Call(id = "c1", state = CallState.ACTIVE, number = "111"))

        callRepository.calls.value = emptyList()

        assertNull(viewModel.ongoingCall.value)
    }

    @Test
    fun `dial sheet closes when a call starts`() = runSubscribed {
        viewModel.onDialpadOpen()

        callRepository.calls.value =
            listOf(Call(id = "c1", state = CallState.DIALING, number = "111"))

        assertFalse(viewModel.uiState.value.isDialpadOpen)
    }

    @Test
    fun `dial sheet stays open while a call only rings`() = runSubscribed {
        viewModel.onDialpadOpen()

        callRepository.calls.value =
            listOf(Call(id = "c1", state = CallState.INCOMING, number = "111"))

        assertTrue(viewModel.uiState.value.isDialpadOpen)
    }

    @Test
    fun `dial sheet can reopen during a call`() = runSubscribed {
        callRepository.calls.value =
            listOf(Call(id = "c1", state = CallState.ACTIVE, number = "111"))

        viewModel.onDialpadOpen()

        assertTrue(viewModel.uiState.value.isDialpadOpen)
    }

    @Test
    fun `dialer default page does not open the sheet during a call`() =
        runTest(mainDispatcherRule.dispatcher) {
            callRepository.calls.value =
                listOf(Call(id = "c1", state = CallState.ACTIVE, number = "111"))
            val viewModel = MainViewModel(
                SavedStateHandle(),
                FakePreferenceRepository(Settings(defaultPage = Page.DIALER)),
                callRepository,
                ClearMissedCalls(telecomRepository, recentRepository),
                FakePhoneRepository(),
                GetPrimaryCall(),
            )
            viewModel.uiState.launchIn(backgroundScope)

            assertFalse(viewModel.uiState.value.isDialpadOpen)
        }

    @Test
    fun `opening missed calls shows recents with nothing covering it`() = runSubscribed {
        viewModel.onTabSelected(MainViewModel.Tab.CONTACTS)
        viewModel.onDialpadOpen()
        viewModel.onSettingsOpen()
        viewModel.search.open()
        viewModel.search.changeText("dana")

        viewModel.onMissedCallsOpened()

        val state = viewModel.uiState.value
        assertEquals(MainViewModel.Tab.RECENTS, state.tab)
        assertFalse(state.isDialpadOpen)
        assertFalse(state.isSettingsOpen)
        assertFalse(state.isSearching)
        assertEquals("", state.searchText)
    }

    @Test
    fun `opening missed calls clears them`() = runSubscribed {
        viewModel.onMissedCallsOpened()

        assertEquals(1, telecomRepository.missedCallsNotificationCancels)
        assertEquals(1, recentRepository.missedCallsReadMarks)
    }
}
