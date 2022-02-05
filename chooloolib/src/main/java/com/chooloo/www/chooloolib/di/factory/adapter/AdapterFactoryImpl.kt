package com.chooloo.www.chooloolib.di.factory.adapter

import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.adapter.PhonesAdapter
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import javax.inject.Inject

class AdapterFactoryImpl @Inject constructor(
    private val phonesInteractor: PhonesInteractor,
    private val recentsInteractor: RecentsInteractor,
    private val stringsInteractor: StringsInteractor,
    private val animationsInteractor: AnimationsInteractor,
    private val preferencesInteractor: PreferencesInteractor
) : AdapterFactory {
    override fun getPhonesAdapter(): PhonesAdapter {
        return PhonesAdapter(
            animationsInteractor,
            stringsInteractor
        )
    }

    override fun getRecentsAdapter(): RecentsAdapter {
        return RecentsAdapter(
            animationsInteractor,
            preferencesInteractor,
            phonesInteractor,
            stringsInteractor,
            recentsInteractor
        )
    }

    override fun getContactsAdapter(): ContactsAdapter {
        return ContactsAdapter(
            animationsInteractor,
            phonesInteractor,
            preferencesInteractor
        )
    }
}