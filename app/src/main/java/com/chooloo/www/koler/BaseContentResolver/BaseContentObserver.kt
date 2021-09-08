package com.chooloo.www.koler.BaseContentResolver

import com.chooloo.www.koler.contentresolver.BaseContentResolver
import com.chooloo.www.koler.util.baseobservable.BaseObservable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseContentObserver<T>(
    private val contentResolver: BaseContentResolver<T>,
    private val disposables: CompositeDisposable
) :
    BaseObservable<BaseContentObserver.Listener<T>>() {

    interface Listener<T> {
        fun onContentChanged(content: T)
    }

    var filter: String?
        get() = contentResolver.filter
        set(value) {
            contentResolver.filter = value
            contentResolver.queryContent { content ->
                listeners.forEach {
                    it.onContentChanged(
                        content
                    )
                }
            }.also {
                disposables.add(it)
            }
        }


    override fun registerListener(listener: Listener<T>) {
        super.registerListener(listener)
        contentResolver.queryContent(listener::onContentChanged).also {
            disposables.add(it)
        }
    }

    override fun onActive() {
        super.onActive()
        val _observer = contentResolver.observeUri {
            contentResolver.queryContent { content ->
                invokeListeners { it.onContentChanged(content) }
            }
        }
    }
}