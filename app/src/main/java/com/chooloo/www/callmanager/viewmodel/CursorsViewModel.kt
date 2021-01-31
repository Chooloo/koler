package com.chooloo.www.callmanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.chooloo.www.callmanager.livedata.ContactsLiveData
import com.chooloo.www.callmanager.livedata.RecentsLiveData

class CursorsViewModel(context: Context) : ViewModel() {
    var contacts: ContactsLiveData
    var recents: RecentsLiveData

    init {
        contacts = ContactsLiveData(context)
        recents = RecentsLiveData(context)
    }
}