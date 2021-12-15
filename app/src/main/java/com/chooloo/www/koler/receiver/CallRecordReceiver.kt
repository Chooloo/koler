package com.chooloo.www.koler.receiver

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractorImpl
import com.chooloo.www.koler.util.PreferencesManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

open class CallRecordReceiver : PhoneCallReceiver() {
    private var audioFile: File? = null
    private var isRecordStarted = false

    override fun onIncomingCallReceived(context: Context, number: String?, start: Date) {
    }

    override fun onIncomingCallAnswered(context: Context, number: String?, start: Date) {
        startRecord(context)
    }

    override fun onIncomingCallEnded(context: Context, number: String?, start: Date, end: Date) {
        stopRecord(context)
    }

    override fun onOutgoingCallStarted(context: Context, number: String?, start: Date) {
        startRecord(context)
    }

    override fun onOutgoingCallEnded(context: Context, number: String?, start: Date, end: Date) {
        stopRecord(context)
    }

    override fun onMissedCall(context: Context, number: String?, start: Date) {
    }

    private fun startRecord(context: Context) {
        try {
            if (!PreferencesManager.getInstance(context).getBoolean(
                    R.string.pref_key_records_enabled,
                    R.bool.pref_default_value_records_enabled
                )
            ) {
                return
            }

            if (isRecordStarted) {
                try {
                    recorder?.stop()  // stop the recording
                } catch (e: RuntimeException) {
                    // RuntimeException is thrown when stop() is called immediately after start().
                    // In this case the output file is not properly constructed ans should be deleted.
                    audioFile?.delete()
                }

                releaseMediaRecorder()
                isRecordStarted = false
            } else {
                if (prepareAudioRecorder(context)) {
                    recorder!!.start()
                    isRecordStarted = true
                    onRecordingStarted(context, audioFile)
                } else {
                    releaseMediaRecorder()
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            releaseMediaRecorder()
        } catch (e: RuntimeException) {
            e.printStackTrace()
            releaseMediaRecorder()
        } catch (e: Exception) {
            e.printStackTrace()
            releaseMediaRecorder()
        }
    }

    private fun stopRecord(context: Context) {
        try {
            if (recorder != null && isRecordStarted) {
                releaseMediaRecorder()
                isRecordStarted = false
                onRecordingFinished(context, audioFile)
            }
        } catch (e: Exception) {
            releaseMediaRecorder()
            e.printStackTrace()
        }
    }

    private fun prepareAudioRecorder(context: Context): Boolean {
        try {
            val sampleDir = File("${Environment.getExternalStorageDirectory().path}/$DIR_NAME")
            if (!sampleDir.exists()) {
                sampleDir.mkdirs()
            }
            val format = PreferencesInteractorImpl(PreferencesManager.getInstance(context)).recordFormat
            val fileName = generateFileName()
            audioFile = File.createTempFile(fileName, format.encoding, sampleDir)
            recorder = MediaRecorder().apply {
                setAudioSource(AUDIO_SOURCE)
                setOutputFormat(format.audioEncoder)
                setAudioEncoder(AUDIO_ENCODER)
                setOutputFile(audioFile!!.absolutePath)
                setOnErrorListener { _, _, _ -> }
            }

            try {
                recorder?.prepare()
            } catch (e: IllegalStateException) {
                releaseMediaRecorder()
                return false
            } catch (e: IOException) {
                releaseMediaRecorder()
                return false
            }

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun releaseMediaRecorder() {
        recorder?.apply {
            reset()
            release()
        }
        recorder = null
    }

    @SuppressLint("SimpleDateFormat")
    private fun generateFileName() =
        "koler_record_" + SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(Date())

    private fun onRecordingStarted(context: Context, audioFile: File?) {}
    protected fun onRecordingFinished(context: Context, audioFile: File?) {}


    companion object {
        const val AUDIO_SOURCE = MediaRecorder.AudioSource.MIC
        const val AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB
        const val DIR_NAME = "kolerRecordings"

        const val ACTION_IN = "android.intent.action.PHONE_STATE"
        const val ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL"
        const val EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER"
        private var recorder: MediaRecorder? = null
    }
}