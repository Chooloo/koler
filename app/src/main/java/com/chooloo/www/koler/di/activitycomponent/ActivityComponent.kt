package com.chooloo.www.koler.di.activitycomponent

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.chooloo.www.koler.di.contextcomponent.ContextComponent
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractorBound
import com.chooloo.www.koler.interactor.dialog.DialogInteractor
import com.chooloo.www.koler.interactor.navigation.NavigationInteractor
import com.chooloo.www.koler.interactor.permission.PermissionInteractor
import com.chooloo.www.koler.interactor.proximity.ProximityInteractor
import com.chooloo.www.koler.interactor.screen.ScreenInteractor
import com.chooloo.www.koler.interactor.sim.SimInteractor
import io.reactivex.disposables.CompositeDisposable

interface ActivityComponent : ContextComponent {
    val disposables: CompositeDisposable

    val lifecycleOwner: LifecycleOwner
    val viewModelStoreOwner: ViewModelStoreOwner

    val simInteractor: SimInteractor
    val dialogInteractor: DialogInteractor
    val screenInteractor: ScreenInteractor
    val proximityInteractor: ProximityInteractor
    val permissionInteractor: PermissionInteractor
    val navigationInteractor: NavigationInteractor
    val callAudioInteractorBound: CallAudioInteractorBound
}