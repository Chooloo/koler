package com.chooloo.www.chooloolib.di.module

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractorImpl
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractorImpl
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractorImpl
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractorImpl
import com.chooloo.www.chooloolib.interactor.screen.ScreensInteractor
import com.chooloo.www.chooloolib.interactor.screen.ScreensInteractorImpl
import com.chooloo.www.chooloolib.interactor.sim.SimsInteractor
import com.chooloo.www.chooloolib.interactor.sim.SimsInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.DisposableContainer

@Module(includes = [ActivityModule.BindsModule::class])
@InstallIn(ActivityComponent::class)
public class ActivityModule {

    @Provides
    fun provideDisposables(): DisposableContainer = CompositeDisposable()

    @Provides
    fun provideFragmentManager(activity: AppCompatActivity): FragmentManager =
        activity.supportFragmentManager

    @Provides
    fun provideLifecycleOwner(activity: AppCompatActivity): LifecycleOwner =
        activity


    @Module
    interface BindsModule {
        @Binds
        fun bindSimsInteractor(simsInteractorImpl: SimsInteractorImpl): SimsInteractor

        @Binds
        fun bindDialogsInteractor(dialogsInteractorImpl: DialogsInteractorImpl): DialogsInteractor

        @Binds
        fun bindScreensInteractor(screensInteractorImpl: ScreensInteractorImpl): ScreensInteractor

        @Binds
        fun bindPromptsInteractor(promptsInteractorImpl: PromptsInteractorImpl): PromptsInteractor

        @Binds
        fun bindPermissionsInteractor(permissionsInteractorImpl: PermissionsInteractorImpl): PermissionsInteractor

        @Binds
        fun bindNavigationsInteractor(navigationsInteractorImpl: NavigationsInteractorImpl): NavigationsInteractor
    }
}