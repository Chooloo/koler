package com.chooloo.www.chooloolib.ui.recents

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.recent.RecentRepository
import com.chooloo.www.chooloolib.ui.common.RecordsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class RecentsViewModel @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val recentRepository: RecentRepository,
    preferenceRepository: PreferenceRepository,
    @ApplicationContext private val context: Context
) : RecordsListViewModel<RecentData, RecentRecord>() {
    val isGroupRecentsEnabled: StateFlow<Boolean> = preferenceRepository.isGroupRecentsEnabled

    override suspend fun enrichItem(item: RecentData): RecentData {
        if (item.number.isEmpty() || (!item.name.isNullOrEmpty() && item.name != item.number && item.typeLabel != null)) {
            return item
        }
        val account = phoneRepository.lookupAccount(item.number) ?: return item
        return item.copy(
            name = account.name ?: item.name ?: item.number,
            photoUri = account.photoUri?.let(android.net.Uri::parse) ?: item.photoUri,
            typeLabel = Phone.getTypeLabel(context.resources, account.type, account.label).toString()
        )
    }

    override suspend fun convertRecordToItem(record: RecentRecord): RecentData {
        val cachedTypeLabel = record.cachedNumberType?.takeIf { it != 0 }?.let { type ->
            Phone.getTypeLabel(context.resources, type, record.cachedNumberLabel).toString()
        }
        return RecentData.fromRecord(record, typeLabel = cachedTypeLabel)
    }

    override fun getRecordsFlow(filter: String?): Flow<List<RecentRecord>> =
        recentRepository.getRecentsFlow(filter)
}
