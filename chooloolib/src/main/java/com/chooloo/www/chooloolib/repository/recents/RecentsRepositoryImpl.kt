package com.chooloo.www.chooloolib.repository.recents

import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.di.factory.livedata.LiveDataFactory
import com.chooloo.www.chooloolib.model.RecentAccount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentsRepositoryImpl @Inject constructor(
    private val liveDataFactory: LiveDataFactory,
    private val contentResolverFactory: ContentResolverFactory
) : RecentsRepository {
    override fun getRecents() = liveDataFactory.getRecentsLiveData()

    override fun getRecent(recentId: Long?, callback: (RecentAccount?) -> Unit) {
        contentResolverFactory.getRecentsContentResolver(recentId).queryItems {
            callback.invoke(it.getOrNull(0))
        }
    }
}