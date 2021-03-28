package com.chooloo.www.koler.util.audio

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager

class AudioManager(private val context: Context) {
    private val _audioManager by lazy { context.getSystemService(AUDIO_SERVICE) as AudioManager }

    enum class AudioMode(val mode: Int) {
        MODE_NORMAL(AudioManager.MODE_NORMAL),
        MODE_IN_CALL(AudioManager.MODE_IN_CALL),
        MODE_CURRENT(AudioManager.MODE_CURRENT),
        MODE_RINGTONE(AudioManager.MODE_RINGTONE),
        MODE_IN_COMMUNICATION(AudioManager.MODE_IN_COMMUNICATION)
    }

    val isPhoneSilent: Boolean
        get() = _audioManager.ringerMode == AudioManager.RINGER_MODE_SILENT ||
                _audioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE

    var audioMode: AudioMode
        get() = AudioMode.values().associateBy(AudioMode::mode)
            .getOrDefault(_audioManager.mode, AudioMode.MODE_NORMAL)
        set(value) {
            _audioManager.mode = value.mode
        }

    var isSpeakerOn: Boolean
        get() = _audioManager.isSpeakerphoneOn
        set(value) {
            _audioManager.isSpeakerphoneOn = value
        }

    var isMuted: Boolean
        get() = _audioManager.isMicrophoneMute
        set(value) {
            _audioManager.isMicrophoneMute = value
        }
}