package com.chooloo.www.koler.ui.menu.recent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.menu.MenuFragment
import com.chooloo.www.koler.util.blockNumber
import com.chooloo.www.koler.util.isNumberBlocked
import com.chooloo.www.koler.util.permissions.runWithPrompt
import com.chooloo.www.koler.util.unblockNumber

class RecentMenuFragment : MenuFragment(), RecentMenuContract.View {
    override val menuRes = R.menu.recent_extra
    private val _number by lazy { argsSafely.getString(ARG_NUMBER) }
    private val _presenter by lazy { RecentMenuPresenter<RecentMenuContract.View>() }

    companion object {
        const val ARG_NUMBER = "number"

        fun newInstance(number: String) = RecentMenuFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_NUMBER, number)
            }
        }
    }

    override fun onSetup() {
        super.onSetup()
        _presenter.attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onAttachMenu(menu: Menu) {
        when {
            _number == null -> menu.apply {
                getItem(R.id.recent_extra_block).isEnabled = false
                removeItem(R.id.recent_extra_unblock)
            }
            _activity.isNumberBlocked(_number!!) -> menu.removeItem(R.id.recent_extra_block)
            else -> menu.removeItem(R.id.recent_extra_unblock)
        }
    }

    override fun onItemClick(item: MenuItem) {
        _presenter.onMenuItemClick(item)
    }

    override fun blockNumber() {
        _number?.let {
            _activity.runWithPrompt(R.string.warning_block_number) {
                _activity.blockNumber(it)
                showMessage(R.string.number_blocked)
            }
        }
    }

    override fun unblockNumber() {
        _number?.let {
            _activity.unblockNumber(it)
            showMessage(R.string.number_unblocked)
        }
    }
}