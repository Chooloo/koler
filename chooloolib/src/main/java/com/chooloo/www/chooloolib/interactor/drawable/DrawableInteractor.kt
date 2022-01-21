package com.chooloo.www.chooloolib.interactor.drawable

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

interface DrawableInteractor {
    interface Listener

    fun getDrawable(@DrawableRes res: Int): Drawable?
}