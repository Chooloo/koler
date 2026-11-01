package com.chooloo.www.chooloolib.di.module

import android.app.ActivityManager
import android.content.Context
import com.chooloo.www.chooloolib.ui.activity.BaseActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object ActivityProvidesModule {
    @Provides
    fun provideBaseActivity(@ActivityContext context: Context): BaseActivity =
        context as BaseActivity

    @Provides
    fun provideActivityManager(@ActivityContext context: Context): ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
}
