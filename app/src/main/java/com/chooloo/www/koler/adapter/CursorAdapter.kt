package com.chooloo.www.koler.adapter

import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class CursorAdapter<VH : RecyclerView.ViewHolder?>(
    protected var context: Context
) : RecyclerView.Adapter<VH>() {

    protected var _cursor: Cursor? = null
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

    var cursor: Cursor?
        get() = _cursor
        set(value) {
            if (value !== _cursor) {
                if (_cursor != null) {
                    unregisterDataSetObserver(_dataSetObserver)
                    _cursor!!.close()
                }
                _cursor = value
                registerDataSetObserver(_dataSetObserver)
            }
        }

    override fun getItemCount() = _cursor?.count ?: 0

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    private fun registerDataSetObserver(dataSetObserver: DataSetObserver) {
        _cursor?.registerDataSetObserver(dataSetObserver)
    }

    private fun unregisterDataSetObserver(dataSetObserver: DataSetObserver) {
        try {
            _cursor?.unregisterDataSetObserver(dataSetObserver)
        } catch (e: IllegalStateException) {
            Timber.e("CursorAdapter was not registered when trying to unregister")
        }
    }
}