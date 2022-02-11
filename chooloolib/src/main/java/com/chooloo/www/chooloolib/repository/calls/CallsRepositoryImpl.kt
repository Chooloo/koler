package com.chooloo.www.chooloolib.repository.calls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.model.Call
import com.chooloo.www.chooloolib.service.CallService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallsRepositoryImpl @Inject constructor() : CallsRepository {
    val calls = MutableLiveData<List<Call>>()

    override fun getCalls(): LiveData<List<Call>> = CallService.sInstance?.calls ?: calls
}