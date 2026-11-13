package com.chooloo.www.chooloolib.ui.caller

sealed interface CallerTarget {
    data class ContactTarget(val contactId: Long) : CallerTarget

    data class RecentTarget(val recentId: Long) : CallerTarget

    data class LinkTarget(val link: String) : CallerTarget
}
