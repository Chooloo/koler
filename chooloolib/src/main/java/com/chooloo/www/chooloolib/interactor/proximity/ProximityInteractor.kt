package com.chooloo.www.chooloolib.interactor.proximity

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface ProximityInteractor : BaseInteractor<ProximityInteractor.Listener> {
    interface Listener

    fun acquire()
    fun release()
}