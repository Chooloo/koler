package com.chooloo.www.chooloolib.adapter

import android.net.Uri
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.chooloolib.util.initials
import javax.inject.Inject


open class ContactsAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val phones: PhonesInteractor,
    private val preferences: PreferencesInteractor,
) : ListAdapter<ContactAccount>(animations) {
    private var _withFavs: Boolean = true
    private var _withHeaders: Boolean = true

    var withFavs: Boolean
        get() = _withFavs
        set(value) {
            _withFavs = value
        }

    var withHeaders: Boolean
        get() = _withHeaders
        set(value) {
            _withHeaders = value
        }


    override fun onBindListItem(listItem: ListItem, item: ContactAccount) {
        listItem.apply {
            titleText = item.name
            isCompact = preferences.isCompact
            phones.getContactAccounts(item.id) { accounts ->
                captionText = accounts?.firstOrNull()?.number
            }

            setImageInitials(item.name?.initials())
            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }

    override fun convertDataToListData(items: List<ContactAccount>) =
        if (_withHeaders) ListData.fromContacts(items, _withFavs) else ListData(items)
}