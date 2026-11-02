package com.chooloo.www.chooloolib.domain.repository

interface AudioRepository {
    suspend fun playDtmfTone(key: Char)
    suspend fun vibrate()
}
