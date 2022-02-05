package com.chooloo.www.chooloolib.ui.recent

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.drawable.DrawablesInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseController
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment
import com.chooloo.www.chooloolib.ui.recents.RecentsFragment
import com.chooloo.www.chooloolib.util.getElapsedTimeString
import javax.inject.Inject

class RecentController @Inject constructor(
    view: RecentContract.View,
    private val phonesInteractor: PhonesInteractor,
    private val recentsInteractor: RecentsInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val promptsInteractor: PromptsInteractor,
    private val blockedInteractor: BlockedInteractor,
    private val drawablesInteractor: DrawablesInteractor,
    private val preferencesInteractor: PreferencesInteractor,
    private val navigationsInteractor: NavigationsInteractor,
    private val permissionsInteractor: PermissionsInteractor
) :
    BaseController<RecentContract.View>(view),
    RecentContract.Controller {

    private val _recent by lazy { recentsInteractor.queryRecent(view.recentId) }

    override fun onSetup() {
        super.onSetup()
        if (_recent == null) return
        val recentCaptions = arrayListOf(_recent!!.relativeTime)
        if (_recent!!.duration > 0) {
            recentCaptions.add(getElapsedTimeString(_recent!!.duration))
        }

        view.apply {
            recentCaption = recentCaptions.joinToString(", ")
            recentImage =
                drawablesInteractor.getDrawable(recentsInteractor.getCallTypeImage(_recent!!.type))
            recentName =
                if (_recent!!.cachedName?.isNotEmpty() == true) _recent!!.cachedName else _recent!!.number

            phonesInteractor.lookupAccount(_recent!!.number) {
                isContactVisible = it?.name != null
                isAddContactVisible = it?.name == null
            }

            isBlockButtonVisible = preferencesInteractor.isShowBlocked
            if (preferencesInteractor.isShowBlocked) {
                permissionsInteractor.runWithDefaultDialer {
                    isBlockButtonActivated = blockedInteractor.isNumberBlocked(_recent!!.number)
                }
            }
        }
    }

    override fun onActionSms() {
        _recent?.let { navigationsInteractor.sendSMS(it.number) }
    }

    override fun onActionCall() {
        _recent?.let { navigationsInteractor.call(it.number) }
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
        _recent?.let { navigationsInteractor.addContact(it.number) }
    }

    override fun onActionOpenContact() {
        _recent?.let {
            phonesInteractor.lookupAccount(it.number) { account ->
                account?.contactId?.let { id ->
                    promptsInteractor.showFragment(BriefContactFragment.newInstance(id))
                }
            }
        }
    }

    override fun onActionShowHistory() {
        _recent?.let {
            promptsInteractor.showFragment(RecentsFragment.newInstance(it.number).apply {
                setOnItemClickListener { recent ->
                    promptsInteractor.showFragment(RecentFragment.newInstance(recent.id))
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