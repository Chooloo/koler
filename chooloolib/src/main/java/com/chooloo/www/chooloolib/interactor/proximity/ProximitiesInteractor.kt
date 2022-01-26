package com.chooloo.www.chooloolib.interactor.proximity

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface ProximitiesInteractor : BaseInteractor<ProximitiesInteractor.Listener> {
    interface Listener

    fun acquire()
    fun release()
}