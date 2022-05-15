package com.chooloo.www.chooloolib.adapter

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.model.PhoneAccount
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PhonesAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    @ApplicationContext private val context: Context
) : ListAdapter<PhoneAccount>(animationsInteractor) {
    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            background = null
            imageVisibility = false
            titleText = item.number
            isRightButtonVisible = true
            captionText = Phone.getTypeLabel(resources, item.type, item.label).toString()

            setTitleBold(true)
            setRightButtonDrawable(R.drawable.round_call_20)
        }
    }

    override fun convertDataToListData(data: List<PhoneAccount>) = ListData.fromPhones(data, context)
}