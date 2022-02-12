package com.chooloo.www.chooloolib.adapter

import android.net.Uri
import android.telecom.Call.Details.CAPABILITY_SEPARATE_FROM_CONFERENCE
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.drawable.DrawablesInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.model.Call
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import javax.inject.Inject

class CallItemsAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    private val phonesInteractor: PhonesInteractor,
    private val colorsInteractor: ColorsInteractor,
    private val drawablesInteractor: DrawablesInteractor,
) : ListAdapter<Call>(animationsInteractor) {
    override fun onBindListItem(listItem: ListItem, item: Call) {
        listItem.apply {
            phonesInteractor.lookupAccount(item.number) { account ->
                account?.photoUri?.let {
                    setImageUri(Uri.parse(it))
                } ?: run {
                    imageDrawable = drawablesInteractor.getDrawable(R.drawable.round_person_20)
                }

                account?.displayString?.let {
                    titleText = it
                    captionText = item.number
                } ?: run {
                    titleText = item.number
                }
            }

//            imageSize = ViewUtils.dpToPx(context, 30).toInt()
            setImageTint(colorsInteractor.getColor(R.color.color_opposite))

            isLeftButtonVisible = true
            isLeftButtonEnabled = item.isCapable(CAPABILITY_SEPARATE_FROM_CONFERENCE)
            setLeftButtonDrawable(R.drawable.round_call_split_24)
            setLeftButtonTintColor(R.color.orange_foreground)
            setLeftButtonBackgroundTintColor(R.color.orange_background)

            isRightButtonVisible = true
            isRightButtonEnabled = true
            setRightButtonDrawable(R.drawable.round_call_end_24)
            setRightButtonTintColor(R.color.red_foreground)
            setRightButtonBackgroundTintColor(R.color.red_background)
        }
    }

    override fun convertDataToListData(data: List<Call>) = ListData(data)
}