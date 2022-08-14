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
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ListItemHolder
import javax.inject.Inject

class CallItemsAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    private val phonesInteractor: PhonesInteractor,
) : ListAdapter<Call>(animationsInteractor) {
    override fun onBindListItem(listItemHolder: ListItemHolder, item: Call) {
        listItemHolder.apply {
            phonesInteractor.lookupAccount(item.number) { account ->
                account?.photoUri?.let {
                    setImageUri(Uri.parse(it))
                } ?: run {
                    setImageResource(R.drawable.person)
                }

                account?.displayString?.let {
                    titleText = it
                    captionText = item.number
                } ?: run {
                    titleText = item.number
                }
            }

            isLeftButtonVisible = true
            isRightButtonVisible = true
            isRightButtonEnabled = true
            isLeftButtonEnabled = item.isCapable(CAPABILITY_SEPARATE_FROM_CONFERENCE)

            setLeftButtonIcon(R.drawable.call_split)
            setRightButtonIcon(R.drawable.call_end)
            setRightButtonIconTint(R.color.negative_foreground)
        }
    }

    override fun convertDataToListData(data: List<Call>) = ListData(data)
}