package com.chooloo.www.chooloolib.ui.common

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R

enum class UserMessage(@StringRes val textRes: Int) {
    CALL_FAILED(R.string.error_call_failed),
    CONTACT_DELETION_FAILED(R.string.error_contact_deletion_failed),
    CONTACT_FAVORITE_CHANGE_FAILED(R.string.error_contact_favorite_change_failed),
    CONTACT_BLOCK_CHANGE_FAILED(R.string.error_contact_block_change_failed),
    CLEAR_RECENTS_FAILED(R.string.error_clear_recents_failed),
}
