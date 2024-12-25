package com.chooloo.www.chooloolib.domain.model

import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord

data class AccountData(
    val contactId: Long,
    val data: String? = null,
    val type: RawContactRecord.RawContactType = RawContactRecord.RawContactType.CUSTOM
)