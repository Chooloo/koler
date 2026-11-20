package com.chooloo.www.koler.ui.call

import android.app.Activity
import android.app.KeyguardManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.ui.components.FloatingBottomBar
import com.chooloo.www.chooloolib.ui.contacts.ContactsScreen
import com.chooloo.www.chooloolib.ui.dialpad.DialpadScreen

@Composable
fun AddCallSheet(
    search: String?,
    onSearchOpen: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSearchClose: () -> Unit,
    onContactClick: (Contact) -> Unit,
) {
    val activity = checkNotNull(LocalActivity.current) { "AddCallSheet needs an activity" }
    LifecycleEventEffect(Lifecycle.Event.ON_STOP) { onSearchClose() }

    Column {
        FloatingBottomBar(
            isSearching = search != null,
            searchText = search.orEmpty(),
            searchHintRes = R.string.hint_search_contacts,
            onSearchOpen = { runUnlocked(activity, onSearchOpen) },
            onSearchTextChange = onSearchChange,
            onSearchClose = onSearchClose,
            content = {},
        )
        if (search == null) {
            DialpadScreen()
        } else {
            ContactsScreen(filter = search, withFavorites = false, onItemClick = onContactClick)
        }
    }
}

private fun runUnlocked(activity: Activity, action: () -> Unit) {
    val keyguardManager = activity.getSystemService(KeyguardManager::class.java)
    if (!keyguardManager.isKeyguardLocked) {
        action()
        return
    }
    keyguardManager.requestDismissKeyguard(
        activity,
        object : KeyguardManager.KeyguardDismissCallback() {
            override fun onDismissSucceeded() = action()
        },
    )
}
