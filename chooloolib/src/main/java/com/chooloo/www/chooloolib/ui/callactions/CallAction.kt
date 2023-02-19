package com.chooloo.www.chooloolib.ui.callactions

import androidx.annotation.DrawableRes

abstract class CallAction {
    var tempIconRes: Int? = null
    var isEnabled: Boolean = true
    var isActivated: Boolean = false
    open val checkedIconRes: Int? = null

    abstract val idRes: Int
    @get:DrawableRes abstract val iconRes: Int
}
