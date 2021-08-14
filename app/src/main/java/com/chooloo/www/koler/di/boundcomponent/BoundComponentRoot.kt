package com.chooloo.www.koler.di.boundcomponent

import com.chooloo.www.koler.interactor.proximity.ProximityInteractor
import com.chooloo.www.koler.interactor.screen.ScreenInteractor

interface BoundComponentRoot {
    val screenInteractor: ScreenInteractor
    val proximityInteractor: ProximityInteractor
}