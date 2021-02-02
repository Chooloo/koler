package com.chooloo.www.callmanager.viewmodel.data

import android.content.Context
import androidx.lifecycle.ViewModel
import com.chooloo.www.callmanager.livedata.ContactsLiveData
import com.chooloo.www.callmanager.livedata.RecentsLiveData

class DataViewModel(context: Context) : ViewModel() {
    var contacts: ContactsLiveData
    var recents: RecentsLiveData

    init {
        contacts = ContactsLiveData(context)
        recents = RecentsLiveData(context)
    }
}