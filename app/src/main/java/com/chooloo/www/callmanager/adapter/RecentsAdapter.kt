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

    private var _onRecentItemClickListener: ((Recent) -> Unit?)? = null
    private var _onRecentItemLongClickListener: ((Recent) -> Unit?)? = null

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
            setOnClickListener { _onRecentItemClickListener?.invoke(recentCall) }
            setOnLongClickListener {
                _onRecentItemLongClickListener?.invoke(recentCall)
                true
            }
            setFadeUpAnimation(this)
        }
    }

    fun updateRecents(newRecents: Array<Recent>) {
        _recents = newRecents
        notifyDataSetChanged()
    }

    fun setOnRecentItemClickListener(onRecentItemClickListener: ((Recent) -> Unit?)?) {
        _onRecentItemClickListener = onRecentItemClickListener
    }

    fun setOnRecentItemLongClickListener(onRecentItemLongClickListener: ((Recent) -> Unit?)?) {
        _onRecentItemLongClickListener = onRecentItemLongClickListener
    }
}