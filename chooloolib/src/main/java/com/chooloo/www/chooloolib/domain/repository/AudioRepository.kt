package com.chooloo.www.chooloolib.domain.repository

interface AudioRepository {
    suspend fun playTone(key: Char)
    suspend fun vibrate()
}
