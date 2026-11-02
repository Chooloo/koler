package com.chooloo.www.chooloolib.ui.caller

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.Phone
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.RecordsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PhonesListViewModel @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val telecomRepository: TelecomRepository,
) : RecordsListViewModel<Phone>() {
    private var _contactId: Long? = null

    fun onContactId(contactId: Long) {
        _contactId = contactId
        updateItemsFlow()
    }

    fun onItemClick(item: Phone) {
        viewModelScope.launch { telecomRepository.callNumber(item.number) }
    }

    override fun getItemsFlow(filter: String?) = _contactId?.let { phoneRepository.phones(it, filter) }
}
