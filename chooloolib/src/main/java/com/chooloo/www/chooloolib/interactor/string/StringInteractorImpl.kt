package com.chooloo.www.chooloolib.interactor.string

import android.content.Context
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable

class StringInteractorImpl(
    internal val context: Context
) : BaseObservable<StringInteractor.Listener>(), StringInteractor {
    override fun getString(stringRes: Int) = context.getString(stringRes)
}