package com.chooloo.www.koler.interactor.recents

import android.content.Context
import androidx.databinding.BaseObservable
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.Recent

class RecentsInteractorImpl(private val context: Context) : BaseObservable(), RecentsInteractor {
    override fun getRecent(recentId: Long): Recent? =
        RecentsContentResolver(context, recentId).content.getOrNull(0)

    override fun deleteRecent(recentId: Long) {
        RecentsContentResolver(context, recentId).delete()
    }
}