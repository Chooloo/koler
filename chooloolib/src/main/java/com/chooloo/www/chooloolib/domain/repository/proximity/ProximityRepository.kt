package com.chooloo.www.chooloolib.domain.repository.proximity

import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface ProximityRepository : BaseRepository {
    fun acquire()
    fun release()
}