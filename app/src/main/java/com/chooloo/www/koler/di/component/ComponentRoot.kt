package com.chooloo.www.koler.di.component

import android.app.KeyguardManager
import android.content.ClipboardManager
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import android.telecom.TelecomManager
import android.telephony.SubscriptionManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.koler.interactor.animation.AnimationInteractor
import com.chooloo.www.koler.interactor.audio.AudioInteractor
import com.chooloo.www.koler.interactor.calls.CallsInteractor
import com.chooloo.www.koler.interactor.color.ColorInteractor
import com.chooloo.www.koler.interactor.contacts.ContactsInteractor
import com.chooloo.www.koler.interactor.drawable.DrawableInteractor
import com.chooloo.www.koler.interactor.numbers.NumbersInteractor
import com.chooloo.www.koler.interactor.phoneaccounts.PhoneAccountsInteractor
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor
import com.chooloo.www.koler.interactor.recents.RecentsInteractor
import com.chooloo.www.koler.interactor.string.StringInteractor
import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.livedata.PhoneProviderLiveData
import com.chooloo.www.koler.livedata.RecentsProviderLiveData
import com.chooloo.www.koler.util.PreferencesManager

interface ComponentRoot {
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

    val phonesProviderLiveData: PhoneProviderLiveData
    val recentsProviderLiveData: RecentsProviderLiveData
    val contactsProviderLiveData: ContactsProviderLiveData

    val colorInteractor: ColorInteractor
    val audioInteractor: AudioInteractor
    val callsInteractor: CallsInteractor
    val stringInteractor: StringInteractor
    val numbersInteractor: NumbersInteractor
    val recentsInteractor: RecentsInteractor
    val drawableInteractor: DrawableInteractor
    val contactsInteractor: ContactsInteractor
    val animationInteractor: AnimationInteractor
    val preferencesInteractor: PreferencesInteractor
    val phoneAccountsInteractor: PhoneAccountsInteractor
}