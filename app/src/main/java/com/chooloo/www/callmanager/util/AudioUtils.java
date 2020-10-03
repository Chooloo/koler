package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.provider.MediaStore;
import android.view.KeyEvent;

import java.util.HashMap;

import timber.log.Timber;

public class AudioUtils {

    private static final int TONE_LENGTH_MS = 150; // The length of DTMF tones in milliseconds
    private static final int TONE_LENGTH_INFINITE = -1;
    private static final int TONE_RELATIVE_VOLUME = 80; // The DTMF tone volume relative to other sounds in the stream
    private static final int DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_DTMF; // Stream type used to play the DTMF tones off call, and mapped to the volume control keys

    private final Object mToneGeneratorLock = new Object();
    private ToneGenerator mToneGenerator;

    public AudioUtils() {
    }

    public static boolean isPhoneSilent(Activity activity) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        return (ringerMode == AudioManager.RINGER_MODE_SILENT) || (ringerMode == AudioManager.RINGER_MODE_VIBRATE);
    }

    public void toggleToneGenerator(boolean toggle) {
        synchronized (mToneGeneratorLock) {
            if (toggle) {
                if (mToneGenerator == null) {
                    try {
                        mToneGenerator = new ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME);
                    } catch (RuntimeException e) {
                        Timber.w(e, "Exception caught while creating local tone generator");
                        mToneGenerator = null;
                    }
                }
            } else {
                if (mToneGenerator != null) {
                    mToneGenerator.release();
                    mToneGenerator = null;
                }
            }
        }
    }

    /**
     * Play the specified tone for the specified milliseconds
     * <p>
     * The tone is played locally, using the audio stream for phone calls.
     * Tones are played only if the "Audible touch tones" user preference
     * is checked, and are NOT played if the device is in silent mode.
     * <p>
     * The tone length can be -1, meaning "keep playing the tone." If the caller does so, it should
     * call stopTone() afterward.
     *
     * @param tone       a tone code from {@link ToneGenerator}
     * @param durationMs tone length.
     */
    private void playTone(int tone, int durationMs, Activity activity) {
        if (tone == -1) return;

        // We need to re-check the ringer mode for *every* playTone() call, rather than keeping
        // a local flag that's updated in onResume(), since it's possible to toggle silent mode
        // without leaving the current activity (via the ENDCALL-longpress menu.)
        if (AudioUtils.isPhoneSilent(activity)) return;
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) return;
            mToneGenerator.startTone(tone, durationMs); // Start the new tone (will stop any playing tone)
        }
    }

    /**
     * Plays the specified tone for TONE_LENGTH_MS milliseconds.
     */
    private void playTone(int tone, Activity activity) {
        playTone(tone, TONE_LENGTH_MS, activity);
    }

    public void playToneByKey(int keyCode, Activity activity) {
        HashMap<Integer, Integer> keyToTone = new HashMap<>();
        keyToTone.put(KeyEvent.KEYCODE_0, ToneGenerator.TONE_DTMF_0);
        keyToTone.put(KeyEvent.KEYCODE_1, ToneGenerator.TONE_DTMF_1);
        keyToTone.put(KeyEvent.KEYCODE_2, ToneGenerator.TONE_DTMF_2);
        keyToTone.put(KeyEvent.KEYCODE_3, ToneGenerator.TONE_DTMF_3);
        keyToTone.put(KeyEvent.KEYCODE_4, ToneGenerator.TONE_DTMF_4);
        keyToTone.put(KeyEvent.KEYCODE_5, ToneGenerator.TONE_DTMF_5);
        keyToTone.put(KeyEvent.KEYCODE_6, ToneGenerator.TONE_DTMF_6);
        keyToTone.put(KeyEvent.KEYCODE_7, ToneGenerator.TONE_DTMF_7);
        keyToTone.put(KeyEvent.KEYCODE_8, ToneGenerator.TONE_DTMF_8);
        keyToTone.put(KeyEvent.KEYCODE_9, ToneGenerator.TONE_DTMF_9);
        keyToTone.put(KeyEvent.KEYCODE_POUND, ToneGenerator.TONE_DTMF_P);
        keyToTone.put(KeyEvent.KEYCODE_STAR, ToneGenerator.TONE_DTMF_S);

        playTone(keyToTone.getOrDefault(keyCode, -1), activity);
    }

    // -- Digits -- //

    /**
     * Stop the tone if it is played.
     * if local tone playback is disabled, just return.
     */
    public void stopTone() {
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator != null) mToneGenerator.stopTone();
        }
    }
}
