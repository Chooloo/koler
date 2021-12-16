package com.chooloo.www.koler.ui.contactsuggestions

import com.chooloo.www.koler.adapter.ContactsSuggestionsAdapter
import com.chooloo.www.koler.ui.contacts.ContactsController

class ContactsSuggestionsController<V : ContactsSuggestionsFragment>(view: V) :
    ContactsController<V>(view) {

    override val adapter by lazy { ContactsSuggestionsAdapter(component) }
}