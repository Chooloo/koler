package com.chooloo.www.koler.interactor.preferences

import android.media.MediaRecorder
import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.base.BaseInteractor

open interface PreferencesInteractor : BaseInteractor<PreferencesInteractor.Listener> {
    interface Listener

    var isRecords: Boolean
    var isCompact: Boolean
    var isAnimations: Boolean
    var isScrollIndicator: Boolean

    var defaultPage: Page
    var accentTheme: AccentTheme
    var recordFormat: RecordFormat


    companion object {
        enum class AccentTheme(val key: String, val theme: Int) {
            RED("red", R.style.Accent_Red),
            BLUE("blue", R.style.Accent_Blue),
            GREEN("green", R.style.Accent_Green),
            ORANGE("orange", R.style.Accent_Orange),
            PURPLE("purple", R.style.Accent_Purple),
            DEFAULT(BLUE.key, BLUE.theme);

            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(AccentTheme::key).getOrDefault(key ?: "", DEFAULT)
            }
        }

        enum class RecordFormat(
            val key: String,
            val outputFormat: Int,
            val audioEncoder: Int,
            val encoding: String
        ) {
            AMR_WB(
                "amr_wb",
                MediaRecorder.OutputFormat.AMR_WB,
                MediaRecorder.AudioEncoder.AMR_WB,
                "amr"
            ),
            MPEG_4(
                "mpeg_4",
                MediaRecorder.OutputFormat.MPEG_4,
                MediaRecorder.AudioEncoder.AAC,
                "mp4"
            ),
            DEFAULT(AMR_WB.key, AMR_WB.outputFormat, AMR_WB.audioEncoder, AMR_WB.encoding);

            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(RecordFormat::key).getOrDefault(key ?: "", DEFAULT)
            }
        }

        enum class Page(val key: String, val index: Int) {
            CONTACTS("contacts", 0),
            RECENTS("recents", 1),
            DEFAULT(CONTACTS.key, CONTACTS.index);

            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(Page::key).getOrDefault(key ?: "", DEFAULT)
            }
        }
    }
}