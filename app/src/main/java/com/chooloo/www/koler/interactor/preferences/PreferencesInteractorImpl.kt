package com.chooloo.www.koler.interactor.preferences

import androidx.databinding.BaseObservable
import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.AccentTheme
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.RecordFormat
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Sim
import com.chooloo.www.koler.util.preferences.PreferencesManager

class PreferencesInteractorImpl(val preferencesManager: PreferencesManager) : BaseObservable(),
    PreferencesInteractor {
    override var isRecords: Boolean

    override var isCompact: Boolean
    override var isAnimations: Boolean
    override var isScrollIndicator: Boolean

    override var sim: Sim
    override var defaultPage: Page
    override var accentTheme: AccentTheme
    override var recordFormat: RecordFormat
}