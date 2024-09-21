package com.chooloo.www.chooloolib.di.module

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.chooloo.www.chooloolib.domain.repository.activity.ActivityRepository
import com.chooloo.www.chooloolib.domain.repository.activity.ActivityRepositoryImpl
import com.chooloo.www.chooloolib.ui.activity.BaseActivity
import dagger.Binds
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
    fun provideFragmentManager(@ActivityContext context: Context): FragmentManager =
        (context as AppCompatActivity).supportFragmentManager

    @Provides
    fun provideActivityManager(@ActivityContext context: Context): ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
}

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityBindsModule {
    @Binds
    abstract fun bindActivityRepository(activityRepository: ActivityRepositoryImpl): ActivityRepository
}
