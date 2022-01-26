package com.chooloo.www.chooloolib.interactor.string

import android.content.Context
import com.chooloo.www.chooloolib.util.baseobservable.BaseObservable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseObservable<StringsInteractor.Listener>(), StringsInteractor {
    override fun getString(stringRes: Int) = context.getString(stringRes)
}