package com.chooloo.www.chooloolib.data.repository.calls

import com.chooloo.www.chooloolib.api.service.CallService
import com.chooloo.www.chooloolib.data.model.Call
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallsRepositoryImpl @Inject constructor() : CallsRepository {
    override fun getCalls(): Flow<List<Call>> = flow {
        CallService.sInstance?.calls?.collect(this::emit)
    }
}