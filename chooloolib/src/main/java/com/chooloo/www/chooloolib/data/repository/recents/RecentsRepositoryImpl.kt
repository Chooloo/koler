package com.chooloo.www.chooloolib.data.repository.recents

import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentsRepositoryImpl @Inject constructor(
    private val contentResolverFactory: ContentResolverFactory
) : RecentsRepository {
    override fun getRecents() = contentResolverFactory.getRecentsContentResolver().getItemsFlow()

    override fun getRecent(recentId: Long?) = flow {
        contentResolverFactory.getRecentsContentResolver(recentId).getItemsFlow().collect {
            emit(it.getOrNull(0))
        }
    }
}