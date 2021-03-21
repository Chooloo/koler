package com.chooloo.www.koler.util.callrecord

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioEncoder
import android.media.MediaRecorder.AudioSource.VOICE_CALL
import android.media.MediaRecorder.OutputFormat
import android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.PreferencesManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class CallRecorder(val context: Context) {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("yy-MM-dd_HH-mm-ss")
    }

    private val _preferences by lazy { PreferencesManager(context) }
    private var _recorder: MediaRecorder? = null
    private var _currentRecording: CallRecording? = null

    val isRecording: Boolean
        get() = _recorder != null

    val activeRecording: CallRecording?
        get() = _currentRecording

    val formatChoice: Int
        get() = _preferences.getInt(R.string.pref_key_record_format, 0)

    fun startRecording(number: String, creationTime: Long): Boolean {
        if (_recorder != null) {
            stopRecording()
        }
        initRecorder()
        if (_recorder == null || !prepareRecorder(number, creationTime)) {
            _recorder?.apply {
                reset()
                release()
            }
            _recorder = null
            return false
        }
        return true
    }

    fun stopRecording(): CallRecording? {
        val recording = _currentRecording
        if (_recorder != null && _currentRecording != null) {
            try {
                resetRecorder()
            } catch (e: IllegalStateException) {
            }
            context.contentResolver.update(
                ContentUris.withAppendedId(EXTERNAL_CONTENT_URI, _currentRecording!!.mediaId),
                CallRecording.generateCompletedValues(),
                null,
                null
            )
            _recorder = null
        }
        return recording
    }

    private fun initRecorder() {
        _recorder = MediaRecorder().apply {
            try {
                setAudioSource(VOICE_CALL)
                setOutputFormat(if (formatChoice == 0) OutputFormat.AMR_WB else OutputFormat.MPEG_4)
                setAudioEncoder(if (formatChoice == 0) AudioEncoder.AMR_WB else AudioEncoder.AAC)
            } catch (e: Exception) {
                reset()
                release()
                _recorder = null
            }
        }
    }

    private fun resetRecorder() {
        _recorder?.apply {
            stop()
            reset()
            release()
        }
    }

    private fun prepareRecorder(number: String, creationTime: Long): Boolean {
        val filename = generateFilename(number)
        val uri = context.contentResolver.insert(
            EXTERNAL_CONTENT_URI,
            CallRecording.generateMediaInsertValues(filename, creationTime)
        ) ?: throw IOException("Generating uri for call recording failed")
        try {
            val pfd = context.contentResolver.openFileDescriptor(uri, "w")
                ?: throw IOException("Opening file for URI $uri failed")
            _recorder?.apply {
                setOutputFile(pfd.fileDescriptor)
                prepare()
                start()
            }
            _currentRecording = CallRecording(
                number = number,
                creationTime = creationTime,
                filename = filename,
                startTime = System.currentTimeMillis(),
                mediaId = uri.lastPathSegment?.toLong() ?: return false
            )
            return true
        } catch (e: IOException) {
            uri.let { context.contentResolver.delete(it, null, null) }
        } catch (e: IllegalStateException) {
            uri.let { context.contentResolver.delete(it, null, null) }
        } catch (e: RuntimeException) {
            uri.let { context.contentResolver.delete(it, null, null) }
            if (e.message?.indexOf("start failed") ?: 0 <= 0) {             // only catch exceptions thrown by the MediaRecorder JNI code
                throw e
            }
        }
        return false
    }

    private fun generateFilename(number: String) =
        "${number}_${DATE_FORMAT.format(Date())}${if (formatChoice == 0) ".amr" else ".m4a"}"

}