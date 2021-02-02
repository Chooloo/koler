package com.chooloo.www.callmanager.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.callmanager.entity.Recent
import com.chooloo.www.callmanager.ui.listitem.ListItem
import com.chooloo.www.callmanager.ui.listitem.ListItemHolder
import com.chooloo.www.callmanager.util.AnimationUtils.setFadeUpAnimation
import com.chooloo.www.callmanager.util.ContactUtils.lookupContact
import com.chooloo.www.callmanager.util.RelativeTime.getTimeAgo
import com.chooloo.www.callmanager.util.Utilities

class RecentsAdapter(
        private val context: Context
) : RecyclerView.Adapter<ListItemHolder>() {

    private var _recents: Array<Recent> = arrayOf()

    private var _onRecentItemClickListener: OnRecentItemClickListener? = null
    private var _onRecentItemLongClickListener: OnRecentItemLongClickListener? = null

    override fun getItemCount(): Int {
        return _recents.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(ListItem(parent.context))
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val recentCall = _recents.get(position)
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

    fun updateRecents(newRecents: Array<Recent>) {
        _recents = newRecents
        notifyDataSetChanged()
    }

    fun setOnRecentItemClickListener(onRecentItemClickListener: OnRecentItemClickListener) {
        _onRecentItemClickListener = onRecentItemClickListener
    }

    fun setOnRecentItemLongClickListener(onRecentItemLongClickListener: OnRecentItemLongClickListener) {
        _onRecentItemLongClickListener = onRecentItemLongClickListener
    }

    interface OnRecentItemClickListener {
        fun onRecentItemClick(recent: Recent)
    }

    interface OnRecentItemLongClickListener {
        fun onRecentItemLongClick(recent: Recent): Boolean
    }
}