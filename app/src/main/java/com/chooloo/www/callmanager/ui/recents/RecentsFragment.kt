package com.chooloo.www.callmanager.ui.recents

import android.Manifest.permission
import android.os.Bundle
import com.chooloo.www.callmanager.adapter.RecentsAdapter
import com.chooloo.www.callmanager.entity.RecentCall
import com.chooloo.www.callmanager.ui.cursor.CursorFragment

class RecentsFragment : CursorFragment<RecentsAdapter>(), RecentsMvpView {
    private lateinit var _presenter: RecentsPresenter<RecentsMvpView>

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(permission.READ_CALL_LOG, permission.WRITE_CALL_LOG)
        private const val ARG_CONTACT_NAME = "contact_name"
        private const val ARG_PHONE_NUMBER = "phone_number"
        fun newInstance(): RecentsFragment {
            return newInstance(null, null)
        }

        fun newInstance(phoneNumber: String?, contactName: String?): RecentsFragment {
            return RecentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHONE_NUMBER, phoneNumber)
                    putString(ARG_CONTACT_NAME, contactName)
                }
            }
        }
    }

    override fun onGetAdapter(): RecentsAdapter {
        return RecentsAdapter(_activity).apply {
            setOnRecentItemClickListener { recentCall: RecentCall? -> _presenter.onRecentItemClick(recentCall) }
            setOnRecentItemLongClickListener { recentCall: RecentCall? -> _presenter.onRecentItemLongClick(recentCall) }
        }
    }

    override fun onSetup() {
        super.onSetup()

        _presenter = RecentsPresenter()
        _presenter.attach(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun openRecent(recentCall: RecentCall) {
        // TODO implement
    }
}