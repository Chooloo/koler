package com.chooloo.www.chooloolib.ui.contactsuggestions

import com.chooloo.www.chooloolib.adapter.ContactsSuggestionsAdapter
import com.chooloo.www.chooloolib.ui.contacts.ContactsController

class ContactsSuggestionsController<V : ContactsSuggestionsFragment>(view: V) :
    ContactsController<V>(view) {

    override val adapter by lazy { ContactsSuggestionsAdapter(component) }
}