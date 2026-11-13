package com.chooloo.www.chooloolib.domain.model

import com.chooloo.www.chooloolib.domain.enum.RecentPresentation
import com.chooloo.www.chooloolib.domain.enum.RecentType

data class Recent(
    val id: Long,
    val number: String,
    val dateMillis: Long,
    val type: RecentType,
    val durationSeconds: Long = 0,
    val name: String? = null,
    val photoUri: String? = null,
    val numberType: Int? = null,
    val numberLabel: String? = null,
    val presentation: RecentPresentation = RecentPresentation.ALLOWED,
)
