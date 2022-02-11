package com.chooloo.www.chooloolib.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.MenuItem
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.google.android.material.internal.ViewUtils
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("RestrictedApi")
class MenuAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    private val colorsInteractor: ColorsInteractor
) : ListAdapter<MenuItem>(animationsInteractor) {

    override fun onBindListItem(listItem: ListItem, item: MenuItem) {
        listItem.apply {
            setBackgroundColor(Color.TRANSPARENT)
            setTitleTextAppearance(R.style.Chooloo_Text_Subtitle1)
            setImageTint(colorsInteractor.getColor(R.color.color_opposite))
            setTitleColor(colorsInteractor.getColor(R.color.color_opposite))

            paddingTop = 28
            paddingBottom = 28
            imageDrawable = item.icon
            titleText = item.title.toString()
            imageSize = ViewUtils.dpToPx(context, 30).toInt()
        }
    }

    override fun convertDataToListData(data: List<MenuItem>) = ListData(data)
}