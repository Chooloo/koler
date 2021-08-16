package com.chooloo.www.koler.di.boundcomponent

import com.chooloo.www.koler.di.component.ComponentRoot
import com.chooloo.www.koler.interactor.proximity.ProximityInteractor
import com.chooloo.www.koler.interactor.screen.ScreenInteractor

interface BoundComponentRoot : ComponentRoot {
    val screenInteractor: ScreenInteractor
    val proximityInteractor: ProximityInteractor
}