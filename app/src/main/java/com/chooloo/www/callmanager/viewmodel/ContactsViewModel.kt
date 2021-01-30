package com.chooloo.www.callmanager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chooloo.www.callmanager.entity.Contact

class ContactsViewModel : ViewModel() {

    private lateinit var _contacts: MutableLiveData<Array<Contact>>
}