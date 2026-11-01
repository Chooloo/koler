package com.chooloo.www.chooloolib.data.repository.blocked

import android.content.ContentValues
import android.content.Context
import android.provider.BlockedNumberContract
import android.provider.BlockedNumberContract.BlockedNumbers
import com.chooloo.www.chooloolib.data.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.domain.model.record.BlockedNumberRecord
import com.chooloo.www.chooloolib.domain.repository.blocked.BlockedRepository
import com.chooloo.www.chooloolib.utils.annotation.RequiresDefaultDialer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class BlockedRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentResolverFactory: ContentResolverFactory,
) : BlockedRepository {
    @RequiresDefaultDialer
    override suspend fun isNumberBlocked(number: String) =
        BlockedNumberContract.isBlocked(context, number)

    @RequiresDefaultDialer
    override suspend fun blockNumber(number: String) {
        if (isNumberBlocked(number)) return
        val contentValues = ContentValues()
        contentValues.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        context.contentResolver.insert(BlockedNumbers.CONTENT_URI, contentValues)
    }

    @RequiresDefaultDialer
    override suspend fun unblockNumber(number: String) {
        if (!isNumberBlocked(number)) return
        BlockedNumberContract.unblock(context, number)
    }

    @RequiresDefaultDialer
    override suspend fun getBlockedNumbers(filter: String?): List<BlockedNumberRecord> =
        contentResolverFactory.getBlockedNumbersContentResolver(filter = filter).getItems()

    @RequiresDefaultDialer
    override fun getBlockedNumbersFlow(filter: String?): Flow<List<BlockedNumberRecord>> =
        contentResolverFactory.getBlockedNumbersContentResolver(filter = filter).getItemsFlow()
}
