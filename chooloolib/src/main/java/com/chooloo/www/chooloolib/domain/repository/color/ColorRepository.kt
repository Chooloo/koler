package com.chooloo.www.chooloolib.domain.repository.color

import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface ColorRepository : BaseRepository {
    suspend fun getColor(@ColorRes colorRes: Int): Int
    suspend fun getAttrColor(@AttrRes colorRes: Int): Int
}