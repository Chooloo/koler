package com.chooloo.www.koler.domain.usecase

import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.repository.CallRepository
import javax.inject.Inject

class SetSpeaker @Inject constructor(private val callRepository: CallRepository) {
    operator fun invoke(isOn: Boolean) {
        when {
            isOn -> callRepository.setAudioRoute(AudioRoute.SPEAKER)

            callRepository.audio.value.route != AudioRoute.BLUETOOTH ->
                callRepository.setAudioRoute(AudioRoute.WIRED_OR_EARPIECE)
        }
    }
}
