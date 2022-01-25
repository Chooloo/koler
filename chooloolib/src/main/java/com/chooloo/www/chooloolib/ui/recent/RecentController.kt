package com.chooloo.www.chooloolib.ui.recent

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseController
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment
import com.chooloo.www.chooloolib.ui.recents.RecentsFragment
import com.chooloo.www.chooloolib.util.getElapsedTimeString
import javax.inject.Inject

class RecentController<V : RecentContract.View> @Inject constructor(
    view: V,
    private val phonesInteractor: PhonesInteractor,
    private val promptInteractor: PromptInteractor,
    private val recentsInteractor: RecentsInteractor,
    private val blockedInteractor: BlockedInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val navigationInteractor: NavigationInteractor,
    private val permissionsInteractor: PermissionsInteractor
) :
    BaseController<V>(view),
    RecentContract.Controller<V> {

    private val _recent by lazy { recentsInteractor.queryRecent(view.recentId) }

    override fun onStart() {
        super.onStart()
        if (_recent == null) return
        val recentCaptions = arrayListOf(_recent!!.relativeTime)
        if (_recent!!.duration > 0) {
            recentCaptions.add(getElapsedTimeString(_recent!!.duration))
        }

        view.apply {
            recentCaption = recentCaptions.joinToString(", ")
            recentImage =
                component.drawables.getDrawable(component.recents.getCallTypeImage(_recent!!.type))
            recentName =
                if (_recent!!.cachedName?.isNotEmpty() == true) _recent!!.cachedName else _recent!!.number

            component.phones.lookupAccount(_recent!!.number) {
                isContactVisible = it?.name != null
                isAddContactVisible = it?.name == null
            }

            isBlockButtonVisible = component.preferences.isShowBlocked
            if (component.preferences.isShowBlocked) {
                component.permissions.runWithDefaultDialer {
                    isBlockButtonActivated = component.blocked.isNumberBlocked(_recent!!.number)
                }
            }
        }
    }

    override fun onActionSms() {
        _recent?.let { navigationInteractor.sendSMS(it.number) }
    }

    override fun onActionCall() {
        _recent?.let { navigationInteractor.call(it.number) }
    }

    override fun onActionDelete() {
        _recent?.let { recent ->
            permissionsInteractor.runWithWriteCallLogPermissions {
                if (it) {
                    dialogsInteractor.askForValidation(R.string.explain_delete_recent) { result ->
                        if (result) {
                            recentsInteractor.deleteRecent(recent.id)
                            view.finish()
                        }
                    }
                }
            }
        }
    }

    override fun onActionAddContact() {
        _recent?.let { navigationInteractor.addContact(it.number) }
    }

    override fun onActionOpenContact() {
        _recent?.let {
            phonesInteractor.lookupAccount(it.number) { account ->
                account?.contactId?.let { id ->
                    promptInteractor.showFragment(BriefContactFragment.newInstance(id))
                }
            }
        }
    }

    override fun onActionShowHistory() {
        _recent?.let {
            promptInteractor.showFragment(RecentsFragment.newInstance(it.number).apply {
                controller.setOnItemClickListener { recent ->
                    component.prompts.showFragment(RecentFragment.newInstance(recent.id))
                }
            })
        }
    }

    override fun onActionBlock(isBlock: Boolean) {
        permissionsInteractor.runWithDefaultDialer {
            _recent?.number?.let {
                if (isBlock) {
                    blockedInteractor.blockNumber(it)
                } else {
                    blockedInteractor.unblockNumber(it)
                }
                view.isBlockButtonActivated = isBlock
            }
        }
    }
}