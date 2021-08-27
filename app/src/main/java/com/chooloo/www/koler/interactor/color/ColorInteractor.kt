package com.chooloo.www.koler.interactor.color

import androidx.annotation.ColorRes
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface ColorInteractor : BaseInteractor<ColorInteractor.Listener> {
    interface Listener

    fun getColor(@ColorRes colorRes: Int): Int
}