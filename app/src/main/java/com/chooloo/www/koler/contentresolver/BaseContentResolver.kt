package com.chooloo.www.koler.contentresolver

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.pushtorefresh.storio3.contentresolver.impl.DefaultStorIOContentResolver
import com.pushtorefresh.storio3.contentresolver.queries.Query
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseContentResolver<T>(private val context: Context) {
    private var _filter: String? = null

    private val _ioContentResolver by lazy {
        DefaultStorIOContentResolver.builder().contentResolver(context.contentResolver).build()
    }

    private val finalUri: Uri
        get() = if (filterUri != null && _filter?.isNotEmpty() == true) {
            Uri.withAppendedPath(filterUri, _filter)
        } else {
            uri
        }

    private val query: Query
        get() = Query.builder()
            .uri(finalUri)
            .columns(*projection)
            .whereArgs(*(selectionArgs ?: arrayOf()))
            .where(if (selection == "") null else selection)
            .sortOrder(if (sortOrder == "") null else sortOrder)
            .build()

    var filter: String?
        get() = _filter
        set(value) {
            _filter = if (value == "") null else value
        }


    fun queryCursor() =
        _ioContentResolver
            .get()
            .cursor()
            .withQuery(query)
            .prepare()
            .executeAsBlocking()

    @SuppressLint("CheckResult")
    fun queryCursor(callback: (Cursor?) -> Unit) =
        _ioContentResolver
            .get()
            .cursor()
            .withQuery(query)
            .prepare()
            .asRxSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callback::invoke)

    fun queryContent() =
        convertCursorToContent(queryCursor())

    fun queryContent(callback: (T) -> Unit) {
        queryCursor { callback.invoke(convertCursorToContent(it)) }
    }


    @SuppressLint("CheckResult")
    fun observeUri(observer: () -> Unit): Disposable = _ioContentResolver
        .observeChangesOfUri(uri, BackpressureStrategy.LATEST)
        .subscribe {
            observer.invoke()
        }

    private fun observeCursor(observer: (Cursor?) -> Unit): Disposable = _ioContentResolver
        .get()
        .cursor()
        .withQuery(query)
        .prepare()
        .asRxFlowable(BackpressureStrategy.LATEST)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer::invoke)

    fun observeContent(observer: (T) -> Unit) =
        observeCursor { observer.invoke(convertCursorToContent(it)) }


    abstract val uri: Uri
    abstract val filterUri: Uri?
    abstract val selection: String?
    abstract val sortOrder: String?
    abstract val projection: Array<String>
    abstract val selectionArgs: Array<String>?
    abstract fun convertCursorToContent(cursor: Cursor?): T
}