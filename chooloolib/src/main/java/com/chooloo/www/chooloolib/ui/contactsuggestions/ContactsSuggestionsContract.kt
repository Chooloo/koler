package com.chooloo.www.chooloolib.ui.contactsuggestions

import com.chooloo.www.chooloolib.ui.contacts.ContactsContract


interface ContactsSuggestionsContract : ContactsContract {
    interface View : ContactsContract.View {
    }

    interface Controller<V : View> : ContactsContract.Controller<V> {
    }
}