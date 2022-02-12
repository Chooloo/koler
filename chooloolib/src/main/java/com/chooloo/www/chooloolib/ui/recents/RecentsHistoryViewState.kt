package com.chooloo.www.chooloolib.ui.recents

import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.repository.recents.RecentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentsHistoryViewState @Inject constructor(
    permissions: PermissionsInteractor,
    recentsRepository: RecentsRepository
) : RecentsViewState(recentsRepository, permissions)