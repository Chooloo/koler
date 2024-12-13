package com.chooloo.www.chooloolib.ui.viewmodel.recents

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.domain.model.record.RecentRecord
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.recent.RecentRepository
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class RecentsViewModelImpl @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val recentRepository: RecentRepository,
    @ApplicationContext private val context: Context
) : RecordsListViewModelImpl<RecentData, RecentRecord>(), RecentsViewModel {
    override suspend fun enrichItem(item: RecentData): RecentData {
        phoneRepository.lookupAccount(item.number)?.let {
            item.name = it.name ?: item.name ?: item.number
            item.typeLabel = Phone.getTypeLabel(context.resources, it.type, it.label).toString()
        }
        return item
    }

    override suspend fun convertRecordToItem(record: RecentRecord) = RecentData(
        id = record.id,
        date = record.date,
        type = record.type,
        number = record.number,
        duration = record.duration,
        groupAccounts = record.groupAccounts,
        name = record.cachedName ?: record.number
    )

    override fun getRecordsFlow(filter: String?): Flow<List<RecentRecord>> =
        recentRepository.getRecentsFlow(filter)
}