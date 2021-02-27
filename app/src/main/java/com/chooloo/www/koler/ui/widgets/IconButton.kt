package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat.getDrawable
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.getAttrColor
import com.google.android.material.floatingactionbutton.FloatingActionButton


@SuppressLint("Recycle", "CustomViewStyleable")
class IconButton : FloatingActionButton {

    @DrawableRes private var _iconDefault: Int? = null
    @DrawableRes private var _iconOnClick: Int? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Koler_IconButton, defStyleRes, 0).also {
            _iconDefault = it.getResourceId(R.styleable.Koler_IconButton_icon, NO_ID)
            _iconOnClick = it.getResourceId(R.styleable.Koler_IconButton_activatedIcon, NO_ID)
        }

        elevation = 0f
        compatElevation = 0f
        imageTintList = ColorStateList.valueOf(context.getAttrColor(R.attr.colorSecondaryVariant))
        backgroundTintList = ColorStateList.valueOf(context.getAttrColor(R.attr.colorSecondary))

        setImageDrawable(_iconDefault?.let { getDrawable(context, it) })
        setOnClickListener {}
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener {
            isActivated = !isActivated
            if (_iconOnClick != NO_ID) {
                (if (isActivated) _iconOnClick else _iconDefault)?.let { setImageResource(it) }
            }
            l?.onClick(it)
        }
    }
}