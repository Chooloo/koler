package com.chooloo.www.chooloolib.repository.recents

import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.model.RecentAccount

interface RecentsRepository {
    fun getRecents(): LiveData<List<RecentAccount>>
    fun getRecent(recentId: Long? = null, callback: (RecentAccount?) -> Unit)
}