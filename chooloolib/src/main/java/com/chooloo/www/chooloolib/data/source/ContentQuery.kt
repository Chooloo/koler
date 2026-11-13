package com.chooloo.www.chooloolib.data.source

import android.net.Uri

class ContentQuery(
    val uri: Uri,
    val projection: Array<String>,
    val selection: String? = null,
    val selectionArgs: Array<String>? = null,
    val sortOrder: String? = null,
)
