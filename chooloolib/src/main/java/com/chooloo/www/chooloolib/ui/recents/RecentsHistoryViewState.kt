package com.chooloo.www.chooloolib.ui.recents

import android.content.ClipboardManager
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.repository.recents.RecentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentsHistoryViewState @Inject constructor(
    clipboardManager: ClipboardManager,
    permissions: PermissionsInteractor,
    recentsRepository: RecentsRepository
) : RecentsViewState(recentsRepository, permissions, clipboardManager)