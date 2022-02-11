package com.chooloo.www.chooloolib.interactor.drawable

import android.content.Context
import androidx.core.content.ContextCompat
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrawablesInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseObservable<DrawablesInteractor.Listener>(), DrawablesInteractor {
    
    override fun getDrawable(res: Int) =
        ContextCompat.getDrawable(context, res)
}