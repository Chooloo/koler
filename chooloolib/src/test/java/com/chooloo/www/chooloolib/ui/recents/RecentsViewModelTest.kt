package com.chooloo.www.chooloolib.ui.recents

import com.chooloo.www.chooloolib.testing.FakePreferenceRepository
import com.chooloo.www.chooloolib.testing.FakeRecentRepository
import com.chooloo.www.chooloolib.testing.MainDispatcherRule
import com.chooloo.www.chooloolib.ui.common.UserMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class RecentsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val recents = FakeRecentRepository()
    private val viewModel by lazy { RecentsViewModel(recents, FakePreferenceRepository()) }

    @Test
    fun `delete confirmed deletes the given calls`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.uiState.launchIn(backgroundScope)

        viewModel.onDeleteConfirmed(listOf(7L, 3L))

        assertEquals(listOf(7L, 3L), recents.deletedRecentIds)
        assertNull(viewModel.uiState.value.message)
    }

    @Test
    fun `delete failure shows message`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.uiState.launchIn(backgroundScope)
        recents.deleteError = SecurityException("no WRITE_CALL_LOG")

        viewModel.onDeleteConfirmed(listOf(7L))

        assertEquals(UserMessage.DELETE_RECENTS_FAILED, viewModel.uiState.value.message)
    }

    @Test
    fun `cancelled delete shows no message`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.uiState.launchIn(backgroundScope)
        recents.deleteError = CancellationException("screen closed")

        viewModel.onDeleteConfirmed(listOf(7L))

        assertNull(viewModel.uiState.value.message)
    }
}
