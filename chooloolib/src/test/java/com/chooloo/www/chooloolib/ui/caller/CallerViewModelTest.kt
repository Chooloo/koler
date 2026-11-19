package com.chooloo.www.chooloolib.ui.caller

import com.chooloo.www.chooloolib.domain.enum.RecentType
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.testing.FakeBlockedRepository
import com.chooloo.www.chooloolib.testing.FakeContactRepository
import com.chooloo.www.chooloolib.testing.FakePhoneRepository
import com.chooloo.www.chooloolib.testing.FakeRecentRepository
import com.chooloo.www.chooloolib.testing.FakeTelecomRepository
import com.chooloo.www.chooloolib.testing.MainDispatcherRule
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CallerViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val number = "0551234567"
    private val recent = Recent(id = 1, number = number, dateMillis = 0, type = RecentType.BLOCKED)
    private val blocked = FakeBlockedRepository(blocked = setOf(number))

    private fun viewModel(isDefaultDialer: Boolean) = CallerViewModel(
        CallerTarget.RecentTarget(recent.id),
        FakePhoneRepository(),
        FakeRecentRepository(recents = listOf(recent)),
        blocked,
        FakeContactRepository(),
        FakeTelecomRepository(defaultDialer = isDefaultDialer),
    )

    @Test
    fun `not default dialer never reads the blocked list`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel(isDefaultDialer = false)
            viewModel.uiState.launchIn(backgroundScope)

            assertEquals(number, viewModel.uiState.value.number)
            assertNull(viewModel.uiState.value.isBlocked)
            assertTrue(blocked.checkedNumbers.isEmpty())
        }

    @Test
    fun `default dialer shows the blocked state`() = runTest(mainDispatcherRule.dispatcher) {
        val viewModel = viewModel(isDefaultDialer = true)
        viewModel.uiState.launchIn(backgroundScope)

        assertEquals(true, viewModel.uiState.value.isBlocked)
        assertEquals(listOf(number), blocked.checkedNumbers)
    }
}
