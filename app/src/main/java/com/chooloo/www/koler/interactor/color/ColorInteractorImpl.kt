package com.chooloo.www.koler.interactor.color

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class ColorInteractorImpl(
    internal val context: Context
) : BaseObservable<ColorInteractor.Listener>(), ColorInteractor {
    override fun getColor(colorRes: Int) = ContextCompat.getColor(context, colorRes)
    override fun getAttrColor(colorRes: Int) =
        TypedValue().also { context.theme.resolveAttribute(colorRes, it, true) }.data
}