package com.chooloo.www.chooloolib.domain.repository.clipboard

import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface ClipboardRepository : BaseRepository {
    val lastCopiedText: String?

    fun copyText(text: String)
}