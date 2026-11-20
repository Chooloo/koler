package com.chooloo.www.chooloolib.ui.caller

import com.chooloo.www.chooloolib.domain.enum.RecentType
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.ContactDetails
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.domain.model.PhoneLookup
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
    private val secondNumber = "0557654321"
    private val contact = Contact(id = 7, name = "Dana", number = number)
    private val recent = Recent(id = 1, number = number, dateMillis = 0, type = RecentType.BLOCKED)
    private val blocked = FakeBlockedRepository(initial = setOf(number))

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

    @Test
    fun `contact shows blocked only when all its numbers are blocked`() =
        runTest(mainDispatcherRule.dispatcher) {
            val blocked = FakeBlockedRepository(initial = setOf(secondNumber))
            val viewModel = contactViewModel(blocked)
            viewModel.uiState.launchIn(backgroundScope)

            assertEquals(false, viewModel.uiState.value.isBlocked)
        }

    @Test
    fun `blocking a contact blocks all its numbers`() = runTest(mainDispatcherRule.dispatcher) {
        val blocked = FakeBlockedRepository(initial = setOf(secondNumber))
        val viewModel = contactViewModel(blocked)
        viewModel.uiState.launchIn(backgroundScope)

        viewModel.onToggleBlocked()

        assertEquals(setOf(number, secondNumber), blocked.blocked)
        assertEquals(true, viewModel.uiState.value.isBlocked)
    }

    @Test
    fun `unblocking a contact unblocks all its numbers`() = runTest(mainDispatcherRule.dispatcher) {
        val blocked = FakeBlockedRepository(initial = setOf(number, secondNumber))
        val viewModel = contactViewModel(blocked)
        viewModel.uiState.launchIn(backgroundScope)

        viewModel.onToggleBlocked()

        assertTrue(blocked.blocked.isEmpty())
        assertEquals(false, viewModel.uiState.value.isBlocked)
    }

    @Test
    fun `blocking from a recent of a saved contact blocks all its numbers`() =
        runTest(mainDispatcherRule.dispatcher) {
            val blocked = FakeBlockedRepository()
            val viewModel = contactViewModel(blocked, CallerTarget.RecentTarget(recent.id))
            viewModel.uiState.launchIn(backgroundScope)

            viewModel.onToggleBlocked()

            assertEquals(setOf(number, secondNumber), blocked.blocked)
        }

    private fun contactViewModel(
        blocked: FakeBlockedRepository,
        target: CallerTarget = CallerTarget.ContactTarget(contact.id),
    ) = CallerViewModel(
        target,
        FakePhoneRepository(lookups = mapOf(number to PhoneLookup(contact.id, contact.name))),
        FakeRecentRepository(recents = listOf(recent)),
        blocked,
        FakeContactRepository(
            contacts = listOf(contact),
            details = mapOf(
                contact.id to ContactDetails(
                    phones = listOf(Phone(number, 0, null), Phone(secondNumber, 0, null)),
                ),
            ),
        ),
        FakeTelecomRepository(defaultDialer = true),
    )
}
