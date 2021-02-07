package com.chooloo.www.koler.adapter

import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class CursorAdapter<VH : RecyclerView.ViewHolder?>(
        protected var context: Context
) : RecyclerView.Adapter<VH>() {

    @JvmField
    protected var cursor: Cursor? = null
    protected var _dataSetObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    fun setCursor(newCursor: Cursor) {
        if (newCursor !== cursor) {
            if (cursor != null) {
                unregisterDataSetObserver(_dataSetObserver)
                cursor!!.close()
            }
            cursor = newCursor
            registerDataSetObserver(_dataSetObserver)
        }
    }

    private fun registerDataSetObserver(dataSetObserver: DataSetObserver) {
        cursor?.registerDataSetObserver(dataSetObserver)
    }

    private fun unregisterDataSetObserver(dataSetObserver: DataSetObserver) {
        try {
            cursor?.unregisterDataSetObserver(dataSetObserver)
        } catch (e: IllegalStateException) {
            Timber.e("CursorAdapter was not registered when trying to unregister")
        }
    }
}