package com.chooloo.www.chooloolib.di.module

import android.app.Activity
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.ui.call.CallActivity
import com.chooloo.www.chooloolib.ui.permissions.PermissionsActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivitiesModule {
    @Provides
    fun provideBaseActivity(activity: Activity): BaseActivity {
        return activity as BaseActivity
    }

    @Provides
    fun provideCallActivity(activity: Activity): CallActivity {
        return activity as CallActivity
    }

    @Provides
    fun providePermissionsActivity(activity: Activity): PermissionsActivity {
        return activity as PermissionsActivity
    }
}