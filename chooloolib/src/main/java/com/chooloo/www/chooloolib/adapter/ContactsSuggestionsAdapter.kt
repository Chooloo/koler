package com.chooloo.www.chooloolib.adapter

import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import javax.inject.Inject

class ContactsSuggestionsAdapter @Inject constructor(
    phonesInteractor: PhonesInteractor,
    animationsInteractor: AnimationsInteractor,
    preferencesInteractor: PreferencesInteractor
) : ContactsAdapter(animationsInteractor, phonesInteractor, preferencesInteractor) {

    override fun onBindListItem(listItem: ListItem, item: ContactAccount) {
        super.onBindListItem(listItem, item)
        listItem.isCompact = true
    }

    override fun convertDataToListData(data: List<ContactAccount>) = ListData.fromContacts(data)
}