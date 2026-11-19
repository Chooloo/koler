package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.CallAlert
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call
import javax.inject.Inject

class GetCallAlert @Inject constructor() {
    operator fun invoke(call: Call, isCallScreenShown: Boolean): CallAlert = when {
        call.state != CallState.INCOMING -> CallAlert.NONE
        isCallScreenShown -> CallAlert.SILENT
        else -> CallAlert.RING
    }
}
