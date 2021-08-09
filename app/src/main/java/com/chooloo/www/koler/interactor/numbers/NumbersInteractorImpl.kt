package com.chooloo.www.koler.interactor.numbers

import android.content.ContentValues
import android.content.Context
import android.provider.BlockedNumberContract
import android.provider.BlockedNumberContract.BlockedNumbers
import androidx.databinding.BaseObservable
import com.chooloo.www.koler.util.annotation.RequiresDefaultDialer

class NumbersInteractorImpl(
    val context: Context
) : BaseObservable(), NumbersInteractor {
    @RequiresDefaultDialer
    override fun isNumberBlocked(number: String) =
        BlockedNumberContract.isBlocked(context, number)

    override fun blockNumber(number: String) {
        if (isNumberBlocked(number)) return
        val contentValues = ContentValues()
        contentValues.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        context.contentResolver.insert(BlockedNumbers.CONTENT_URI, contentValues)
    }

    override fun unblockNumber(number: String) {
        if (!isNumberBlocked(number)) return
        val contentValues = ContentValues()
        contentValues.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        context.contentResolver.insert(BlockedNumbers.CONTENT_URI, contentValues)?.also {
            context.contentResolver.delete(it, null, null)
        }
    }
}