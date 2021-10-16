package com.chooloo.www.koler.interactor.drawable

import android.content.Context
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class DrawableInteractorImpl(
    internal val context: Context
) : BaseObservable<DrawableInteractor.Listener>(), DrawableInteractor {
    override fun getDrawable(res: Int) =
        ContextCompat.getDrawable(context, res)
}