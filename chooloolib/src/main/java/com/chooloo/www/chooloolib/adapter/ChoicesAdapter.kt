package com.chooloo.www.chooloolib.adapter

import android.annotation.SuppressLint
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.drawable.DrawablesInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.google.android.material.internal.ViewUtils
import javax.inject.Inject

class ChoicesAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    private val colorsInteractor: ColorsInteractor,
    private val drawablesInteractor: DrawablesInteractor
) : ListAdapter<String>(animationsInteractor) {
    @SuppressLint("RestrictedApi")
    override fun onBindListItem(listItem: ListItem, item: String) {
        listItem.apply {
            setTitleBold(true)
            setImageTint(colorsInteractor.getColor(R.color.color_opposite))

            titleText = item
            imageSize = ViewUtils.dpToPx(context, 30).toInt()
            imageDrawable = drawablesInteractor.getDrawable(R.drawable.round_fiber_manual_record_20)
        }
    }

    override fun convertDataToListData(data: List<String>) = ListData(data)
}