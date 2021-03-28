package com.chooloo.www.koler.util.callrecord

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioSource
import android.widget.Toast
import com.chooloo.www.koler.util.preferences.KolerPreferences
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.RecordFormat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class CallRecorder(val context: Context) {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("yy-MM-dd_HH-mm-ss")
        const val OUTPUT_DIR = "Koler Recordings"
    }

    private val _preferences by lazy { KolerPreferences(context) }
    private var _recorder: MediaRecorder? = null
    private var _currentRecording: CallRecording? = null

    val formatChoice: RecordFormat
        get() = _preferences.recordFormat

    fun startRecording(number: String): Boolean {
        if (_recorder != null) {
            stopRecording()
        }
        initRecorder(number)
        return try {
            _recorder?.start()
            true
        } catch (e: Exception) {
            _recorder = null
            false
        }
    }

    fun stopRecording(): CallRecording? {
        _recorder?.apply {
            stop()
            release()
            _recorder = null
        }
        return _currentRecording.also {
            _currentRecording = null
        }
    }

    private fun initRecorder(number: String) {
        val filename = generateFilename(number)
        _recorder = MediaRecorder().apply {
            setAudioSource(AudioSource.VOICE_COMMUNICATION);
            setOutputFormat(formatChoice.outputFormat)
            setAudioEncoder(formatChoice.audioEncoder)
            setOutputFile(filename)
            prepare()
            _currentRecording = CallRecording(
                number = number,
                creationTime = System.currentTimeMillis(),
                filename = filename
            )
        }
    }

    private fun generateFilename(number: String): String {
        val folder = File(context.externalCacheDir, OUTPUT_DIR)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return ("${folder.absolutePath}${File.separator}Koler_${number}_${DATE_FORMAT.format(Date())}.${formatChoice.encoding}").also {
            Toast.makeText(context, "Saving recording at $it", Toast.LENGTH_LONG).show()
        }
    }
}