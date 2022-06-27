package com.chooloo.www.chooloolib.util

import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton


@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int?) {
    if (resource == 0) {
        return
    }
    resource?.let { imageView.setImageResource(it) }
}

@BindingAdapter("app:icon")
fun setIconRes(button: Button, iconRes: Int?) {
    if (iconRes == 0) {
        return
    }
    iconRes?.let { (button as MaterialButton).setIconResource(it) }
}

@BindingAdapter("app:iconTint")
fun setIconTintRes(button: Button, tintRes: Int?) {
    if (tintRes == 0) {
        return
    }
    tintRes?.let { (button as MaterialButton).setIconTintResource(it) }
}
