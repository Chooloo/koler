package com.chooloo.www.chooloolib.di.activitycomponent

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.chooloo.www.chooloolib.di.contextcomponent.ContextComponent
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.proximity.ProximityInteractor
import com.chooloo.www.chooloolib.interactor.screen.ScreenInteractor
import com.chooloo.www.chooloolib.interactor.sim.SimInteractor
import io.reactivex.disposables.CompositeDisposable

interface ActivityComponent : ContextComponent {
    val disposables: CompositeDisposable

    val lifecycleOwner: LifecycleOwner
    val viewModelStoreOwner: ViewModelStoreOwner

    val sims: SimInteractor
    val dialogs: DialogsInteractor
    val screens: ScreenInteractor
    val proximities: ProximityInteractor
    val permissions: PermissionsInteractor
    val navigations: NavigationInteractor
}