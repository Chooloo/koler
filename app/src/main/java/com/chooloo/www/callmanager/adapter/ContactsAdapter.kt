package com.chooloo.www.callmanager.adapter

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader
import com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.Companion.getContactFromCursor
import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.ui.listitem.ListItem
import com.chooloo.www.callmanager.ui.listitem.ListItemHolder
import com.chooloo.www.callmanager.util.AnimationUtils.setFadeUpAnimation

class ContactsAdapter(context: Context) : CursorAdapter<ListItemHolder?>(context) {
    private var _headersCounts: Array<Int> = arrayOf()
    private var _headers: Array<String> = arrayOf()

    private var _onContactItemClickListener: OnContactItemClickListener? = null
    private var _onContactItemLongClickListener: OnContactItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(ListItem(parent.context))
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        cursor?.moveToPosition(position)

        val contact = getContactFromCursor(cursor)
        holder.listItem.apply {
            setBigText(contact.name)
            setHeaderText(getHeader(position))
            showHeader(isFirstInHeader(position))
            setOnClickListener { _onContactItemClickListener?.onContactItemClick(contact) }
            setOnLongClickListener { _onContactItemLongClickListener?.onContactItemLongClick(contact) == true }
            if (contact.photoUri != null) {
                setImageUri(Uri.parse(contact.photoUri))
            } else {
                setImageBackgroundColor(ContextCompat.getColor(context, R.color.grey_100))
            }
            setFadeUpAnimation(this)
        }
    }

    override fun setCursor(newCursor: Cursor) {
        super.setCursor(newCursor)
        _headersCounts = newCursor.extras.getIntArray(ContactsCursorLoader.EXTRA_INDEX_COUNTS)?.toTypedArray()
                ?: arrayOf()
        _headers = newCursor.extras.getStringArray(ContactsCursorLoader.EXTRA_INDEX_TITLES)
                ?: arrayOf()
    }

    private fun isFirstInHeader(position: Int): Boolean {
        var total = 0
        _headersCounts.forEach { count ->
            when {
                position == total -> return true
                else -> total += count
            }
        }
        return false
    }

    private fun getHeader(position: Int): String {
        var total = 0
        _headersCounts.indices.forEach { i ->
            when {
                position <= total -> return _headers[i]
                else -> total += _headersCounts[i]
            }
        }
        return ""
    }

    fun setOnContactItemClick(onContactItemClickListener: OnContactItemClickListener) {
        _onContactItemClickListener = onContactItemClickListener
    }

    fun setOnContactItemLongClickListener(onContactItemLongClickListener: OnContactItemLongClickListener) {
        _onContactItemLongClickListener = onContactItemLongClickListener
    }

    interface OnContactItemClickListener {
        fun onContactItemClick(contact: Contact)
    }

    interface OnContactItemLongClickListener {
        fun onContactItemLongClick(contact: Contact): Boolean
    }
}