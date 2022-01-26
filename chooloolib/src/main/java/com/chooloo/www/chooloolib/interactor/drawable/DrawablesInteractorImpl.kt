package com.chooloo.www.chooloolib.interactor.drawable

import android.content.Context
import androidx.core.content.ContextCompat
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrawablesInteractorImpl @Inject constructor(
    internal val context: Context
) : BaseObservable<DrawablesInteractor.Listener>(), DrawablesInteractor {
    override fun getDrawable(res: Int) =
        ContextCompat.getDrawable(context, res)
}