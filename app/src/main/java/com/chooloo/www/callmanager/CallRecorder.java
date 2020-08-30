package com.chooloo.www.callmanager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.DEFAULT;
    private static final int AUDIO_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;
    private static final int AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;
    private static final String FILE_TYPE = "3gpp";

    private String mFileName;
    private boolean mIsRecording = false;
    private Context mContext;
    private MediaRecorder mRecorder;
    private File mFile;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CallRecorder(Context context, @Nullable String name) {
        mContext = context;

        if (name != null) mFileName = name + '.' + FILE_TYPE;
        else mFileName = generateFileName();

        File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        mFile = new File(downloadPath, mFileName);
        Timber.d("Path of file %s", mFile.getPath());

        try {
            if (mFile.exists()) mFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // define recorder
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(AUDIO_SOURCE);
        mRecorder.setOutputFormat(AUDIO_FORMAT);
        mRecorder.setAudioEncoder(AUDIO_ENCODER);
        mRecorder.setOutputFile(mFile);
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
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                Toast.makeText(mContext, "Call recording ended", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mRecorder = null;
        }

        if (mIsRecording) mIsRecording = false;
    }

    @SuppressLint("SimpleDateFormat")
    private static String generateFileName() {
        return "koler_record_" + new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date()) + "." + FILE_TYPE;
    }
}
