package com.chooloo.www.chooloolib.adapter

import android.net.Uri
import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.chooloolib.util.initials
import javax.inject.Inject

open class ContactsAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    private val phonesInteractor: PhonesInteractor,
    private val preferencesInteractor: PreferencesInteractor,
) : ListAdapter<ContactAccount>(animationsInteractor) {

    override fun onBindListItem(listItem: ListItem, item: ContactAccount) {
        listItem.apply {
            titleText = item.name
            isCompact = preferencesInteractor.isCompact
            phonesInteractor.getContactAccounts(item.id) { accounts ->
                captionText = accounts?.firstOrNull()?.number
            }

            setImageInitials(item.name?.initials())
            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }

    override fun convertDataToListData(data: List<ContactAccount>) =
        ListData.fromContacts(data, true)
}