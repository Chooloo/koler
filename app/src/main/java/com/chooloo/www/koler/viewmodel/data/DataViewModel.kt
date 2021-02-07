package com.chooloo.www.koler.viewmodel.data

import android.content.Context
import androidx.lifecycle.ViewModel
import com.chooloo.www.koler.livedata.ContactsLiveData
import com.chooloo.www.koler.livedata.RecentsLiveData

class DataViewModel(context: Context) : ViewModel() {
    var contacts: ContactsLiveData
    var recents: RecentsLiveData

    init {
        contacts = ContactsLiveData(context)
        recents = RecentsLiveData(context)
    }
}