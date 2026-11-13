package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.model.PhoneLookup
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository

class FakePhoneRepository(private val lookups: Map<String, PhoneLookup> = emptyMap()) :
    PhoneRepository {
    override suspend fun lookup(number: String): PhoneLookup? = lookups[number]
}
