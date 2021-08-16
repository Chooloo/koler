package com.chooloo.www.koler.interactor.color

import android.content.Context
import com.chooloo.www.koler.util.BaseObservable

class ColorInteractorImpl(
    internal val context: Context
) : BaseObservable<ColorInteractor.Listener>(), ColorInteractor {
    override fun getColor(colorRes: Int) = context.getColor(colorRes)
}