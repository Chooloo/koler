package com.chooloo.www.koler.ui.call

import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.chooloolib.testing.FakeAudioRepository
import com.chooloo.www.chooloolib.testing.FakePhoneRepository
import com.chooloo.www.chooloolib.testing.FakePreferenceRepository
import com.chooloo.www.chooloolib.testing.MainDispatcherRule
import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.enum.CallCapability
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.enum.CallsState
import com.chooloo.www.koler.domain.model.Call
import com.chooloo.www.koler.domain.model.CallAudio
import com.chooloo.www.koler.domain.usecase.GetCallsState
import com.chooloo.www.koler.domain.usecase.GetPrimaryCall
import com.chooloo.www.koler.domain.usecase.SetSpeaker
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CallViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val callRepository = FakeCallRepository()
    private val lookup = PhoneLookup(contactId = 1, name = "Dana")
    private val viewModel by lazy {
        CallViewModel(
            callRepository,
            FakePhoneRepository(mapOf("111" to lookup)),
            FakeAudioRepository(),
            FakePreferenceRepository(),
            GetPrimaryCall(),
            GetCallsState(),
            SetSpeaker(callRepository),
        )
    }

    private fun activeCall(id: String = "c1", state: CallState = CallState.ACTIVE) = Call(
        id = id,
        state = state,
        number = "111",
        capabilities = setOf(CallCapability.MUTE, CallCapability.HOLD),
    )

    private fun runSubscribed(block: suspend () -> Unit) = runTest(mainDispatcherRule.dispatcher) {
        viewModel.uiState.launchIn(backgroundScope)
        block()
    }

    @Test
    fun `state follows the primary call and looks up the caller`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall())

        val state = viewModel.uiState.value
        assertEquals("c1", state.call?.id)
        assertEquals(CallsState.ACTIVE, state.callsState)
        assertEquals(lookup, state.caller)
    }

    @Test
    fun `mute and hold toggle from their current state`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall(state = CallState.HOLDING))
        callRepository.audio.value = CallAudio(isMuted = true)

        viewModel.onCallActionClick(CallActionType.MUTE)
        viewModel.onCallActionClick(CallActionType.HOLD)

        assertEquals(listOf("muted false", "unhold c1"), callRepository.actions)
    }

    @Test
    fun `speaker toggles directly without bluetooth`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall())
        callRepository.audio.value =
            CallAudio(
                route = AudioRoute.EARPIECE,
                supportedRoutes = listOf(AudioRoute.EARPIECE, AudioRoute.SPEAKER),
            )

        viewModel.onCallActionClick(CallActionType.SPEAKER)

        assertEquals(listOf("route SPEAKER"), callRepository.actions)
    }

    @Test
    fun `speaker opens the route picker when bluetooth is available`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall())
        callRepository.audio.value =
            CallAudio(supportedRoutes = listOf(AudioRoute.SPEAKER, AudioRoute.BLUETOOTH))

        viewModel.onCallActionClick(CallActionType.SPEAKER)

        assertEquals(CallViewModel.Sheet.AUDIO_ROUTE, viewModel.uiState.value.sheet)
        assertTrue(callRepository.actions.isEmpty())
    }

    @Test
    fun `actions reflect capabilities and active state`() = runSubscribed {
        callRepository.calls.value =
            listOf(activeCall().copy(capabilities = setOf(CallCapability.MUTE)))
        callRepository.audio.value = CallAudio(isMuted = true, route = AudioRoute.SPEAKER)

        val actions = viewModel.uiState.value.actions.associateBy { it.type }
        assertEquals(true, actions.getValue(CallActionType.MUTE).isActive)
        assertEquals(false, actions.getValue(CallActionType.HOLD).isEnabled)
        assertEquals(true, actions.getValue(CallActionType.SPEAKER).isActive)
    }

    @Test
    fun `multiple calls show swap and merge instead of speaker`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall("c1"), activeCall("c2", CallState.HOLDING))

        val types = viewModel.uiState.value.actions.map { it.type }
        assertTrue(CallActionType.SWAP in types && CallActionType.MERGE in types)
        assertTrue(CallActionType.SPEAKER !in types)
    }

    @Test
    fun `keypad keys play dtmf on the primary call and build the keypad text`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall())

        viewModel.onKeypadKeyClick('5')
        viewModel.onKeypadKeyClick('#')

        assertEquals("5#", viewModel.uiState.value.keypadValue)
        assertEquals(listOf("dtmf c1 5", "dtmf c1 #"), callRepository.actions)
    }

    @Test
    fun `dismissing the phone account picker declines the pending call`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall(state = CallState.SELECT_PHONE_ACCOUNT))

        viewModel.onPhoneAccountDismiss()

        assertEquals(listOf("decline c1"), callRepository.actions)
    }

    @Test
    fun `add call sheet closes when the new call is added`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall())
        viewModel.onCallActionClick(CallActionType.ADD_CALL)

        callRepository.calls.value =
            listOf(
                activeCall(state = CallState.HOLDING),
                activeCall(id = "c2", state = CallState.DIALING),
            )

        assertEquals(null, viewModel.uiState.value.sheet)
    }

    @Test
    fun `keypad sheet stays open when a call is added`() = runSubscribed {
        callRepository.calls.value = listOf(activeCall())
        viewModel.onShowKeypad()

        callRepository.calls.value =
            listOf(activeCall(), activeCall(id = "c2", state = CallState.INCOMING))

        assertEquals(CallViewModel.Sheet.KEYPAD, viewModel.uiState.value.sheet)
    }
}
