package com.chooloo.www.koler.interactor.audio

import com.chooloo.www.koler.interactor.base.BaseInteractor

interface AudioInteractor : BaseInteractor<AudioInteractor.Listener> {
    interface Listener

    var isMuted: Boolean
    var isSilent: Boolean
    var isSpeakerOn: Boolean
    var audioMode: AudioMode

    fun playTone(tone: Int)
    fun playToneByKey(keyCode: Int)
    fun playTone(tone: Int, durationMs: Int)

    fun vibrate(millis: Long = 10)

    
    enum class AudioMode(val mode: Int) {
        NORMAL(android.media.AudioManager.MODE_NORMAL),
        IN_CALL(android.media.AudioManager.MODE_IN_CALL),
        CURRENT(android.media.AudioManager.MODE_CURRENT),
        RINGTONE(android.media.AudioManager.MODE_RINGTONE),
        IN_COMMUNICATION(android.media.AudioManager.MODE_IN_COMMUNICATION)
    }


    companion object {
        const val SHORT_VIBRATE_LENGTH: Long = 20
    }
}