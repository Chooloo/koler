package com.chooloo.www.callmanager.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader.Companion.getRecentCallFromCursor
import com.chooloo.www.callmanager.entity.RecentCall
import com.chooloo.www.callmanager.ui.listitem.ListItem
import com.chooloo.www.callmanager.ui.listitem.ListItemHolder
import com.chooloo.www.callmanager.util.AnimationUtils.setFadeUpAnimation
import com.chooloo.www.callmanager.util.ContactUtils.lookupContact
import com.chooloo.www.callmanager.util.RelativeTime.getTimeAgo
import com.chooloo.www.callmanager.util.Utilities

class RecentsAdapter(
        context: Context
) : CursorAdapter<ListItemHolder?>(context) {

    private var _onRecentItemClickListener: OnRecentItemClickListener? = null
    private var _onRecentItemLongClickListener: OnRecentItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(ListItem(parent.context))
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        cursor?.moveToPosition(position)

        val recentCall = getRecentCallFromCursor(cursor)
        val contact = lookupContact(context, recentCall.callerNumber)

        holder.listItem.run {
            setBigText(contact.name ?: recentCall.callerNumber)
            setSmallText(if (recentCall.callDate != null) getTimeAgo(recentCall.callDate.getTime()) else null)
            setImageDrawable(ContextCompat.getDrawable(context, Utilities.getCallTypeImage(recentCall.callType)))
            setOnClickListener { _onRecentItemClickListener?.onRecentItemClick(recentCall) }
            setOnLongClickListener { _onRecentItemLongClickListener?.onRecentItemLongClick(recentCall) == true }
            setFadeUpAnimation(this)
        }
    }

    fun setOnRecentItemClickListener(onRecentItemClickListener: OnRecentItemClickListener) {
        _onRecentItemClickListener = onRecentItemClickListener
    }

    fun setOnRecentItemLongClickListener(onRecentItemLongClickListener: OnRecentItemLongClickListener) {
        _onRecentItemLongClickListener = onRecentItemLongClickListener
    }

    interface OnRecentItemClickListener {
        fun onRecentItemClick(recentCall: RecentCall?)
    }

    interface OnRecentItemLongClickListener {
        fun onRecentItemLongClick(recentCall: RecentCall?): Boolean
    }
}