package com.chooloo.www.chooloolib.data.repository.calls

import com.chooloo.www.chooloolib.data.model.Call
import kotlinx.coroutines.flow.Flow

interface CallsRepository {
    fun getCalls(): Flow<List<Call>>
}