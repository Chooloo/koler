package com.chooloo.www.chooloolib.adapter

import android.annotation.SuppressLint
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.google.android.material.internal.ViewUtils
import javax.inject.Inject

@SuppressLint("RestrictedApi")
class ChoicesAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val colors: ColorsInteractor,
) : ListAdapter<String>(animations) {

    override fun onBindListItem(listItem: ListItem, item: String) {
        listItem.apply {
            setTitleBold(true)
            setTitleColor(colors.getAttrColor(R.attr.colorOnSecondary))

            titleText = item
            imageVisibility = false
            imageSize = ViewUtils.dpToPx(context, 30).toInt()
        }
    }

    override fun convertDataToListData(data: List<String>) = ListData(data)
}