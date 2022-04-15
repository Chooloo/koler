package com.chooloo.www.chooloolib.ui.recentshistory

import android.content.ClipboardManager
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.repository.recents.RecentsRepository
import com.chooloo.www.chooloolib.ui.recents.RecentsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentsHistoryViewState @Inject constructor(
    permissions: PermissionsInteractor,
    clipboardManager: ClipboardManager,
    recentsRepository: RecentsRepository
) : RecentsViewState(recentsRepository, permissions, clipboardManager) {
}