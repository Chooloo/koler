package com.chooloo.www.koler.interactor.view

import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface ViewInteractor : BaseInteractor<ViewInteractor.Listener> {
    interface Listener

    val navBarHeight: Int
    val hasNavBar: Boolean
    val selectableItemBackgroundDrawable: Drawable?
    val selectableItemBackgroundBorderlessDrawable: Drawable?

    fun getSizeInDp(sizeInDp: Int): Int

    @ColorInt
    fun getAttrColor(@AttrRes attrRes: Int): Int
}