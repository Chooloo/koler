package com.chooloo.www.koler.util.preferences

import android.content.Context
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
        }

        enum class Sim(val key: String, val number: Int) {
            TRY("Asdas", 1)
        }

        enum class RecordFormat(val key: String, val format: String) {
            TRY("23092", "asdsa")
        }
    }

    private val _pref by lazy { PreferencesManager(context) }

    //region values
    var accentTheme: AccentTheme
        get() {
            val prefAccentKeyRes = _pref.getString(R.string.pref_key_color, AccentTheme.BLUE.key)
            AccentTheme.values().forEach {
                if (it.key == prefAccentKeyRes) {
                    return it
                }
            }
            return AccentTheme.BLUE
        }
        set(value) {
            _pref.putString(R.string.pref_key_color, value.key)
        }

    var sim: Sim
        get() = Sim.TRY
        set(value) {
            _pref.putString(R.string.pref_key_sim_select, value.key)
        }


    var recordFormat: RecordFormat
        get() = RecordFormat.TRY
        set(value) {
            _pref.putString(R.string.pref_key_record_format, value.key)
        }
    //endregion
}