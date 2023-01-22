package com.chooloo.www.chooloolib.adapter

import android.net.Uri
import android.telecom.Call.Details.CAPABILITY_SEPARATE_FROM_CONFERENCE
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.model.Call
import com.chooloo.www.chooloolib.data.model.ListData
import com.chooloo.www.chooloolib.di.module.IoScope
import com.chooloo.www.chooloolib.di.module.MainScope
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ListItemHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CallItemsAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    @IoScope private val ioScope: CoroutineScope,
    private val phonesInteractor: PhonesInteractor,
    @MainScope private val mainScope: CoroutineScope,
) : ListAdapter<Call>(animationsInteractor) {
    override fun onBindListItem(listItemHolder: ListItemHolder, item: Call) {
        listItemHolder.apply {
            ioScope.launch {
                val account = phonesInteractor.lookupAccount(item.number)

                mainScope.launch {
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
            }

            isLeftButtonVisible = true
            isRightButtonVisible = true
            isRightButtonEnabled = true
            isLeftButtonEnabled = item.isCapable(CAPABILITY_SEPARATE_FROM_CONFERENCE)

            setLeftButtonIcon(R.drawable.call_split)
            setRightButtonIcon(R.drawable.call_end)
            setRightButtonIconTint(R.color.negative_background)
        }
    }

    override fun convertDataToListData(items: List<Call>) = ListData(items)
}