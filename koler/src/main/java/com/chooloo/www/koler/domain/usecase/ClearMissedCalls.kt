package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import javax.inject.Inject

class ClearMissedCalls @Inject constructor(
    private val telecomRepository: TelecomRepository,
    private val recentRepository: RecentRepository,
) {
    // Telecom leaves the call log to the dialer; cancelling first keeps a call missed in between notified.
    suspend operator fun invoke() {
        telecomRepository.cancelMissedCallsNotification()
        recentRepository.markMissedCallsRead()
    }
}
