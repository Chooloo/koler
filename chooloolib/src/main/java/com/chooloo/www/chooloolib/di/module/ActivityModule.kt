package com.chooloo.www.chooloolib.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractorImpl
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractorImpl
import com.chooloo.www.chooloolib.interactor.screen.ScreensInteractor
import com.chooloo.www.chooloolib.interactor.screen.ScreensInteractorImpl
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@InstallIn(ActivityComponent::class)
@Module(includes = [ActivityModule.BindsModule::class])
class ActivityModule {
    @Provides
    fun provideBaseActivity(@ActivityContext context: Context): BaseActivity<*> =
        context as BaseActivity<*>

    @Provides
    fun provideFragmentManager(@ActivityContext context: Context): FragmentManager =
        (context as AppCompatActivity).supportFragmentManager

    @Module
    @InstallIn(ActivityComponent::class)
    interface BindsModule {
        @Binds
        fun bindDialogsInteractor(dialogsInteractorImpl: DialogsInteractorImpl): DialogsInteractor

        @Binds
        fun bindScreensInteractor(screensInteractorImpl: ScreensInteractorImpl): ScreensInteractor

        @Binds
        fun bindPromptsInteractor(promptsInteractorImpl: PromptsInteractorImpl): PromptsInteractor
    }
}