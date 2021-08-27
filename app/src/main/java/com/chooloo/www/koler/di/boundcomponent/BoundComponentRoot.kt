package com.chooloo.www.koler.di.boundcomponent

import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.koler.di.component.ComponentRoot
import com.chooloo.www.koler.interactor.permission.PermissionInteractor
import com.chooloo.www.koler.interactor.proximity.ProximityInteractor
import com.chooloo.www.koler.interactor.screen.ScreenInteractor
import com.chooloo.www.koler.ui.navigation.NavigationInteractor

interface BoundComponentRoot : ComponentRoot {
    val lifecycleOwner: LifecycleOwner

    val screenInteractor: ScreenInteractor
    val proximityInteractor: ProximityInteractor
    val permissionInteractor: PermissionInteractor
    val navigationInteractor: NavigationInteractor
}