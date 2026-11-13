package com.chooloo.www.chooloolib.data.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.provider.BlockedNumberContract
import android.provider.BlockedNumberContract.BlockedNumbers
import com.chooloo.www.chooloolib.data.source.ContentException
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.repository.BlockedRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@Singleton
class BlockedRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BlockedRepository {
    override suspend fun isBlocked(number: String): Boolean = withContext(ioDispatcher) {
        BlockedNumberContract.isBlocked(context, number)
    }

    override suspend fun block(number: String) = withContext(ioDispatcher) {
        if (BlockedNumberContract.isBlocked(context, number)) return@withContext
        val values = ContentValues().apply { put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number) }
        if (contentResolver.insert(BlockedNumbers.CONTENT_URI, values) == null) {
            throw ContentException.InsertFailed(BlockedNumbers.CONTENT_URI)
        }
    }

    override suspend fun unblock(number: String) {
        withContext(ioDispatcher) { BlockedNumberContract.unblock(context, number) }
    }
}
