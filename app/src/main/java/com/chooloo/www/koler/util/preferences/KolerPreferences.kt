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
            DEFAULT(BLUE.key, BLUE.theme)
        }

        enum class RecordFormat(
            val key: String,
            val outputFormat: Int,
            val audioEncoder: Int,
            val encoding: String
        ) {
            AMR_WB("amr_wb", OutputFormat.AMR_WB, AudioEncoder.AMR_WB, "amr"),
            MPEG_4("mpeg_4", OutputFormat.MPEG_4, AudioEncoder.AAC, "mp4"),
            DEFAULT(AMR_WB.key, AMR_WB.outputFormat, AMR_WB.audioEncoder, AMR_WB.encoding)
        }

        enum class Sim(val key: String, val number: Int) {
            TRY("Asdas", 1),
            DEFAULT(TRY.key, TRY.number)
        }
    }

    private val _pref by lazy { PreferencesManager(context) }

    //region values
    var accentTheme: AccentTheme
        get() = AccentTheme.values().associateBy(AccentTheme::key)[_pref.getString(
            R.string.pref_key_color,
            AccentTheme.DEFAULT.key
        )]!!
        set(value) {
            _pref.putString(R.string.pref_key_color, value.key)
        }

    var sim: Sim
        get() = Sim.values().associateBy(Sim::key)[_pref.getString(
            R.string.pref_key_sim_select,
            Sim.DEFAULT.key
        )]!!
        set(value) {
            _pref.putString(R.string.pref_key_sim_select, value.key)
        }

    var recordFormat: RecordFormat
        get() = RecordFormat.values().associateBy(RecordFormat::key)[_pref.getString(
            R.string.pref_key_record_format,
            RecordFormat.DEFAULT.key
        )]!!
        set(value) {
            _pref.putString(R.string.pref_key_record_format, value.key)
        }

    var isCompact: Boolean
        get() = _pref.getBoolean(
            R.string.pref_key_compact,
            context.resources.getBoolean(R.bool.pref_default_value_compact)
        )
        set(value) {
            _pref.putBoolean(R.string.pref_key_compact, value)
        }
    //endregion
}