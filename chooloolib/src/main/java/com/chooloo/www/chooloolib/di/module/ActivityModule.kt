package com.chooloo.www.chooloolib.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.chooloolib.di.factory.adapter.AdapterFactory
import com.chooloo.www.chooloolib.di.factory.adapter.AdapterFactoryImpl
import com.chooloo.www.chooloolib.di.factory.controller.ControllerFactory
import com.chooloo.www.chooloolib.di.factory.controller.ControllerFactoryImpl
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactoryImpl
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
import dagger.hilt.android.qualifiers.ActivityContext
import io.reactivex.disposables.CompositeDisposable

@Module(includes = [ActivityModule.BindsModule::class])
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun provideDisposables(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideFragmentManager(@ActivityContext activity: Context): FragmentManager =
        (activity as AppCompatActivity).supportFragmentManager

    @Provides
    fun provideLifecycleOwner(@ActivityContext activity: Context): LifecycleOwner =
        activity as AppCompatActivity

    @Module
    @InstallIn(ActivityComponent::class)
    interface BindsModule {
        @Binds
        fun bindAdapterFactory(adapterFactoryImpl: AdapterFactoryImpl): AdapterFactory

        @Binds
        fun bindFragmentsFactory(fragmentsFactoryImpl: FragmentFactoryImpl): FragmentFactory

        @Binds
        fun bindControllerFactory(controllerFactoryImpl: ControllerFactoryImpl): ControllerFactory


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