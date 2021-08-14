package com.chooloo.www.koler.call

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioSource
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.RecordFormat
import java.io.IOException
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class CallRecorder(val context: Context) {
    private var _recorder: MediaRecorder? = null
    private var _currentRecording: CallRecording? = null
    private val formatChoice: RecordFormat
        get() = (context.applicationContext as KolerApp).componentRoot.preferencesInteractor.recordFormat

    private fun resetRecorder() {
        _recorder?.apply {
            reset()
            release()
        }
        _recorder = null
    }

    private fun generateFilename(number: String): String {
        return "blabla"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initRecording(number: String): Boolean {
        val filename = generateFilename(number)
        val recordingUri = context.contentResolver.insert(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            CallRecording.generateMediaInsertValues(filename, System.currentTimeMillis())
        )

        return try {
            val pfd = recordingUri?.let { context.contentResolver.openFileDescriptor(it, "w") }
                ?: throw IOException("Opening file for URI $recordingUri failed")

            resetRecorder()
            _recorder?.apply {
                setAudioSource(AudioSource.VOICE_RECOGNITION)
                setOutputFormat(formatChoice.outputFormat)
                setAudioEncoder(formatChoice.audioEncoder)
                setOutputFile(pfd.fileDescriptor)
            }

            _currentRecording = CallRecording(
                number = number,
                filename = filename,
                creationTime = System.currentTimeMillis(),
                mediaId = recordingUri.lastPathSegment?.toLong()
            )

            Toast.makeText(context, "Starting recording at $filename", Toast.LENGTH_LONG).show()
            true
        } catch (e: Exception) {
            recordingUri?.let { context.contentResolver.delete(it, null, null) }
            if (e.message?.indexOf("start failed") ?: -1 >= 0) {
                throw e
            }
            resetRecorder()
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun startRecording(number: String): Boolean {
        return try {
            resetRecorder()
            initRecording(number)
            _recorder?.start()
            true
        } catch (e: Exception) {
            resetRecorder()
            false
        }
    }

    fun stopRecording(): CallRecording? {
        _recorder?.stop()
        resetRecorder()
        return _currentRecording.also {
            _currentRecording = null
        }
    }


    companion object {
        val DATE_FORMAT = SimpleDateFormat("yy-MM-dd_HH-mm-ss")
        const val OUTPUT_DIR = "Koler Recordings"
    }
}