package com.chooloo.www.chooloolib.ui.briefcontact.menu

import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.ui.base.menu.BaseMenuFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BriefContactMenuFragment @Inject constructor() : BaseMenuFragment() {
    override val viewState: BriefContactMenuViewState by activityViewModels()

    @Inject
    lateinit var dialogs: DialogsInteractor

    @Inject
    lateinit var prompts: PromptsInteractor


    override fun onSetup() {
        super.onSetup()
        viewState.apply {
            isFavorite.observe(this@BriefContactMenuFragment) {
                changeItemVisibility(R.id.menu_brief_contact_set_favorite, !it)
                changeItemVisibility(R.id.menu_brief_contact_unset_favorite, it)
            }

            showHistoryEvent.observe(this@BriefContactMenuFragment) {
                it.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getRecentsHistoryFragment(viewState.contactName.value))
                }
            }

            confirmDeleteEvent.observe(this@BriefContactMenuFragment) {
                it.ifNew?.let {
                    dialogs.askForValidation(R.string.explain_delete_contact) { bl ->
                        if (bl) viewState.onDelete()
                    }
                }
            }
        }
    }
}