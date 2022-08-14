package com.chooloo.www.chooloolib.ui.recent.menu

import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.ui.base.menu.BaseMenuFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecentMenuFragment @Inject constructor() : BaseMenuFragment() {
    override val viewState: RecentMenuViewState by activityViewModels()

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var dialogs: DialogsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory

    override fun onSetup() {
        super.onSetup()
        viewState.apply {
            showHistoryEvent.observe(this@RecentMenuFragment) { ev ->
                ev.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getRecentsHistoryFragment(viewState.recentNumber.value))
                }
            }

            isBlocked.observe(this@RecentMenuFragment) {
                changeItemVisibility(R.id.menu_recent_block, !it)
                changeItemVisibility(R.id.menu_recent_unblock, it)
            }
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem) {
        if (menuItem.itemId == R.id.menu_brief_contact_delete) {
            dialogs.askForValidation(R.string.explain_delete_contact) { bl ->
                if (bl) viewState.onDelete()
            }
        }
        super.onMenuItemClick(menuItem)
    }
}