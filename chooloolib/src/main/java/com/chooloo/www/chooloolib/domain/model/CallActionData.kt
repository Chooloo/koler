package com.chooloo.www.chooloolib.domain.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.chooloo.www.chooloolib.utils.LoadingState

data class CallActionData(
    val icon: ImageVector,
    val action: CallActionType,
    @StringRes val text: Int,
    val isEnabled: Boolean = false,
    val isActivated: Boolean = false,
    val counterAction: CallActionType? = null,
    val disabledIcon: ImageVector? = null,
    val activatedIcon: ImageVector? = null,
    @StringRes val disabledText: Int? = null,
    @StringRes val activatedText: Int? = null,
    val loadingState: LoadingState = LoadingState.IDLE,
)