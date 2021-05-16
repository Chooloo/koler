package com.chooloo.www.koler.util.preferences

import android.content.Context
import android.media.MediaRecorder.AudioEncoder
import android.media.MediaRecorder.OutputFormat
import com.chooloo.www.koler.R

class KolerPreferences(
    private val context: Context
) {

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
            AMR_WB("amr_wb", OutputFormat.AMR_WB, AudioEncoder.AMR_WB, "amr"),
            MPEG_4("mpeg_4", OutputFormat.MPEG_4, AudioEncoder.AAC, "mp4"),
            DEFAULT(AMR_WB.key, AMR_WB.outputFormat, AMR_WB.audioEncoder, AMR_WB.encoding);

            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(RecordFormat::key).getOrDefault(key ?: "", DEFAULT)
            }
        }

        enum class Sim(val key: String, val number: Int) {
            TRY("Asdas", 1),
            DEFAULT(TRY.key, TRY.number);

            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(Sim::key).getOrDefault(key ?: "", DEFAULT)
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

    private val _pref by lazy { PreferencesManager(context) }

    //region values
    var sim: Sim
        get() = Sim.fromKey(_pref.getString(R.string.pref_key_sim_select))
        set(value) {
            _pref.putString(R.string.pref_key_sim_select, value.key)
        }

    var accentTheme: AccentTheme
        get() = AccentTheme.fromKey(
            _pref.getString(
                R.string.pref_key_color,
                AccentTheme.DEFAULT.key
            )
        )
        set(value) {
            _pref.putString(R.string.pref_key_color, value.key)
        }

    var recordFormat: RecordFormat
        get() = RecordFormat.fromKey(_pref.getString(R.string.pref_key_record_format))
        set(value) {
            _pref.putString(R.string.pref_key_record_format, value.key)
        }

    var defaultPage: Page
        get() = Page.fromKey(_pref.getString(R.string.pref_key_default_page))
        set(value) {
            _pref.putString(R.string.pref_key_default_page, value.key)
        }

    var isCompact: Boolean
        get() = _pref.getBoolean(
            R.string.pref_key_compact,
            context.resources.getBoolean(R.bool.pref_default_value_compact)
        )
        set(value) {
            _pref.putBoolean(R.string.pref_key_compact, value)
        }

    var isAnimations: Boolean
        get() = _pref.getBoolean(
            R.string.pref_key_animations,
            context.resources.getBoolean(R.bool.pref_default_value_animations)
        )
        set(value) {
            _pref.putBoolean(R.string.pref_key_animations, value)
        }

    var recordsEnabled: Boolean
        get() = _pref.getBoolean(
            R.string.pref_key_records_enabled,
            context.resources.getBoolean(R.bool.pref_default_value_records_enabled)
        )
        set(value) {
            _pref.putBoolean(R.string.pref_key_records_enabled, value)
        }

    var showedDefaultDialerBlockedNotice: Boolean
        get() = _pref.getBoolean(
            R.string.pref_key_default_dialer_blocked_notice,
            context.resources.getBoolean(R.bool.pref_showed_default_value_default_dialer_blocked_notice)
        )
        set(value) {
            _pref.putBoolean(R.string.pref_key_default_dialer_blocked_notice, value)
        }
    //endregion
}