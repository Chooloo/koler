package com.chooloo.www.koler.domain.model

import com.chooloo.www.koler.domain.enum.AudioRoute

data class CallAudio(
    val isMuted: Boolean = false,
    val route: AudioRoute? = null,
    val supportedRoutes: List<AudioRoute> = emptyList(),
)
