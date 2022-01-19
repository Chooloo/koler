package com.chooloo.www.chooloolib.ui.dialer

import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.ui.dialpad.DialpadContract

class DialerContract : DialpadContract {
    interface View : DialpadContract.View {
        val suggestionsCount: Int
        var isSuggestionsVisible: Boolean
        var isAddContactButtonVisible: Boolean

        fun setSuggestionsFilter(filter: String)
    }

    interface Controller<V : View> : DialpadContract.Controller<V> {
        fun onCallClick()
        fun onAddContactClick()
        fun onSuggestionsChanged(contacts: List<ContactAccount>)
    }
}