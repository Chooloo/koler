package com.chooloo.www.chooloolib.domain.repository

import com.chooloo.www.chooloolib.domain.model.PhoneLookup

interface PhoneRepository {
    suspend fun lookup(number: String): PhoneLookup?
}
