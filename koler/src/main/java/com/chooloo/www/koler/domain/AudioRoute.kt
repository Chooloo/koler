package com.chooloo.www.koler.domain

enum class AudioRoute { EARPIECE, SPEAKER, BLUETOOTH, WIRED_HEADSET, WIRED_OR_EARPIECE }

data class CallAudio(
    val isMuted: Boolean = false,
    val route: AudioRoute? = null,
    val supportedRoutes: List<AudioRoute> = emptyList(),
)
