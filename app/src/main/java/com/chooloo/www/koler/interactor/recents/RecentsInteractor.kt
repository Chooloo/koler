package com.chooloo.www.koler.interactor.recents

import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface RecentsInteractor : BaseInteractor<RecentsInteractor.Listener> {
    interface Listener

    fun getRecent(recentId: Long): Recent?
    fun deleteRecent(recentId: Long)
}