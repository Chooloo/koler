package com.chooloo.www.chooloolib.repository.calls

import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.model.Call

interface CallsRepository {
    fun getCalls(): LiveData<List<Call>>
}