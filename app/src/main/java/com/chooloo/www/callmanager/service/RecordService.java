package com.chooloo.www.callmanager.service;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import timber.log.Timber;

@SuppressLint("Registered")
public class RecordService extends Service {

    public static final int RECORD_SERVICE_START = 1;
    public static final int RECORD_SERVICE_STOP = 2;

    // Recorder Constants
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int AUDIO_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;
    private static final int AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;
    private static final String FILE_TYPE = "3gpp";

    private Context mContext;
    private String mFileName;
    private boolean mIsRecording = false;
    private MediaRecorder mRecorder;
    private File mFile;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();

        Timber.d("RecordService OnStartCommand");
        if (intent == null) return START_NOT_STICKY;

        int commandType = intent.getIntExtra("commandType", 0);
        if (commandType == 0) return START_NOT_STICKY;

        switch (commandType) {
            case RECORD_SERVICE_START:
                start();
                break;
            case RECORD_SERVICE_STOP:
                stop();
                break;
        }
        return START_STICKY;
    }

    private void deleteFile() {
        if (mFile == null) return;
        mFile.delete();
        mFile = null;
    }

    private void terminate() {
        Timber.i("Terminating call recorder");
        stop();
        deleteFile();
        mIsRecording = false;
    }

    /**
     * Start recordingT
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void start() {
//        if (name != null) mFileName = name + '.' + FILE_TYPE;
//        else mFileName = generateFileName();
        mFileName = generateFileName();

        File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        mFile = new File(downloadPath, mFileName);
        Timber.d("Path of file %s", mFile.getPath());

        // define recorder
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(AUDIO_SOURCE);
        mRecorder.setOutputFormat(AUDIO_FORMAT);
        mRecorder.setAudioEncoder(AUDIO_ENCODER);
        mRecorder.setOutputFile(mFile);

        mRecorder.setOnErrorListener((mr, what, extra) -> {
            Timber.e("Recorder OnErrorListener " + what + ", " + extra);
            terminate();
        });

        mRecorder.setOnInfoListener((mr, what, extra) -> {
            Timber.e("Recorder OnInfoListener " + what + ", " + extra);
            terminate();
        });

        try {
            mRecorder.prepare();
            mRecorder.start();
            mIsRecording = true;

            Timber.i("Call recording started");
            Toast.makeText(mContext, "Recording call...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Timber.e(e, "Couldn't start call recording");
            Toast.makeText(mContext, "Couldn't start call recording :(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            terminate();
        }
    }

    /**
     * Stop and release recording
     */
    public void stop() {
        if (mRecorder == null) return;

        try {
            Toast.makeText(mContext, "Call recording ended", Toast.LENGTH_SHORT).show();
            mRecorder.stop();
        } catch (IllegalStateException e) {
            Toast.makeText(mContext, "Couldn't record call", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            deleteFile();
        }

        mRecorder.release();
        mRecorder = null;

        if (mIsRecording) mIsRecording = false;
    }

    @SuppressLint("SimpleDateFormat")
    private static String generateFileName() {
        return "koler_record_" + new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date()) + "." + FILE_TYPE;
    }
}
