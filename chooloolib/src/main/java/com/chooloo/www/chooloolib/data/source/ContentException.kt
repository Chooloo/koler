package com.chooloo.www.chooloolib.data.source

import android.net.Uri

sealed class ContentException(val uri: Uri) : Exception() {
    override val message: String get() = "${javaClass.simpleName}(uri=$uri)"

    class ProviderUnavailable(uri: Uri) : ContentException(uri)

    class InsertFailed(uri: Uri) : ContentException(uri)

    class RowNotChanged(uri: Uri, val id: Long) : ContentException(uri) {
        override val message: String get() = "${javaClass.simpleName}(uri=$uri, id=$id)"
    }
}
