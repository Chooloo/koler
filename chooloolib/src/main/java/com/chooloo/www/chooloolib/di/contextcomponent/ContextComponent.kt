package com.chooloo.www.chooloolib.di.contextcomponent

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import android.os.VibratorManager
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.di.livedatafactory.LiveDataFactory
import com.chooloo.www.chooloolib.interactor.animation.AnimationInteractor
import com.chooloo.www.chooloolib.interactor.audio.AudioInteractor
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudioInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorInteractor
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.drawable.DrawableInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringInteractor
import com.chooloo.www.chooloolib.util.PreferencesManager

interface ContextComponent {
    val liveDataFactory: LiveDataFactory

    val vibrator: Vibrator
    val powerManager: PowerManager
    val audioManager: AudioManager
    val telecomManager: TelecomManager
    val keyguardManager: KeyguardManager
    val clipboardManager: ClipboardManager
    val inputMethodManager: InputMethodManager
    val preferencesManager: PreferencesManager
    val subscriptionManager: SubscriptionManager
    val notificationManager: NotificationManagerCompat

    val calls: CallsInteractor
    val colors: ColorInteractor
    val audios: AudioInteractor
    val phones: PhonesInteractor
    val strings: StringInteractor
    val blocked: BlockedInteractor
    val recents: RecentsInteractor
    val drawables: DrawableInteractor
    val contacts: ContactsInteractor
    val animations: AnimationInteractor
    val callAudios: CallAudioInteractor
    val preferences: PreferencesInteractor
}