package com.chooloo.www.koler.di.module

import com.chooloo.www.chooloolib.di.module.ActivityModule
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractorImpl
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractorImpl
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractorImpl
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module(includes = [ActivityModule.BindsModule::class])
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Module
    @InstallIn(ActivityComponent::class)
    interface BindsModule {
    }
}