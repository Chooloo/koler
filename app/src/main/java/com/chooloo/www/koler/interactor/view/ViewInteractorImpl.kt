package com.chooloo.www.koler.interactor.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.interactor.base.BaseInteractorImpl


class ViewInteractorImpl(
    private val context: Context
) : BaseInteractorImpl<ViewInteractor.Listener>(), ViewInteractor {

    override val navBarHeight: Int
        get() {
            val resource =
                context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resource > 0) context.resources.getDimensionPixelSize(resource) else 0
        }

    override val hasNavBar: Boolean
        get() {
            val resource =
                context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
            return resource > 0 && context.resources.getBoolean(resource)
        }

    override val selectableItemBackgroundDrawable: Drawable?
        get() {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                typedValue,
                true
            )
            return ContextCompat.getDrawable(context, typedValue.resourceId)
        }

    override val selectableItemBackgroundBorderlessDrawable: Drawable?
        get() {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackgroundBorderless,
                typedValue,
                true
            )
            return ContextCompat.getDrawable(context, typedValue.resourceId)
        }


    override fun getSizeInDp(sizeInDp: Int): Int =
        (sizeInDp * context.resources.displayMetrics.density + 0.5f).toInt()

    @ColorInt
    override fun getAttrColor(attrRes: Int): Int =
        TypedValue().also { context.theme.resolveAttribute(attrRes, it, true) }.data
}