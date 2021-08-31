package com.chooloo.www.koler.interactor.string

import android.content.Context
import com.chooloo.www.koler.util.baseobservable.BaseObservable

class StringInteractorImpl(
    internal val context: Context
) : BaseObservable<StringInteractor.Listener>(), StringInteractor {
    override fun getString(stringRes: Int) = context.getString(stringRes)
}