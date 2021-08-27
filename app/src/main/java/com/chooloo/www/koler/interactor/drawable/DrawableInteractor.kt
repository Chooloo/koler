package com.chooloo.www.koler.interactor.drawable

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

interface DrawableInteractor {
    interface Listener

    fun getDrawable(@DrawableRes res: Int): Drawable?
}