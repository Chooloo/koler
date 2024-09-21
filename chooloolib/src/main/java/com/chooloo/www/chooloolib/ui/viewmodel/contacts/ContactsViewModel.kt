package com.chooloo.www.chooloolib.ui.viewmodel.contacts

import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.record.ContactRecord
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModel

interface ContactsViewModel : RecordsListViewModel<ContactData, ContactRecord>
