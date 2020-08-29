package com.chooloo.www.callmanager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.chooloo.www.callmanager.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import timber.log.Timber;

public class CallRecorder {

    private static final String LOG_TAG = "CallRecorder";

    // audio constants
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.VOICE_CALL;
    private static final int AUDIO_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;
    private static final int AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;
    private static String mFileName;

    private boolean mIsRecording = false;
    private Context mContext;
    private MediaRecorder mRecorder;

    @SuppressLint("SimpleDateFormat")
    public CallRecorder(Context context) {
        mContext = context;
        mFileName = Objects.requireNonNull(mContext.getExternalCacheDir()).getAbsolutePath();
        mFileName += "/koler_record_" + new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date()) + ".3gpp";

        // define recorder
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(AUDIO_SOURCE);
        mRecorder.setOutputFormat(AUDIO_FORMAT);
        mRecorder.setAudioEncoder(AUDIO_ENCODER);
        mRecorder.setOutputFile(mFileName);
    }


    /**
     * Start recording
     */
    public void start() {
        try {
            mRecorder.prepare();
            mRecorder.start();
            mIsRecording = true;
            Toast.makeText(mContext, "Recording call...", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Couldn't start call recording :(", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Stop and release recording
     */
    public void stop() {
        mRecorder.stop();
        mRecorder.release();
        Timber.i("CALLRECORDPATH " + mFileName);
        if (mIsRecording) mIsRecording = false;
    }

}
