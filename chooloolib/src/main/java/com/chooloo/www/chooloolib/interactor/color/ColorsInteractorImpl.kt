package com.chooloo.www.chooloolib.interactor.color

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseObservable<ColorsInteractor.Listener>(), ColorsInteractor {
    override fun getColor(colorRes: Int) = ContextCompat.getColor(context, colorRes)
    override fun getAttrColor(colorRes: Int) =
        TypedValue().also { context.theme.resolveAttribute(colorRes, it, true) }.data
}