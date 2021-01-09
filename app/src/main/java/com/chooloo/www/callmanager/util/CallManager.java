package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.widget.Toast;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.entity.Contact;

import org.jetbrains.annotations.NotNull;

import java.net.URLDecoder;
import java.util.List;

import timber.log.Timber;


public class CallManager {

    // Variables
    public static Call sCall;
    private static List<Contact> sAutoCallingContactsList = null;
    private static boolean sIsAutoCalling = false;
    private static int sAutoCallPosition = 0;

    // -- Call Actions -- //

    /**
     * Call a given number
     *
     * @param activity activity
     * @param number   number to call to
     */
    public static void call(@NotNull Activity activity, @NotNull String number) {
        if (PermissionUtils.checkDefaultDialer(activity)) {
            try {
                // initiate variables
                TelecomManager telecomManager = (TelecomManager) activity.getSystemService(Context.TELECOM_SERVICE);
                assert telecomManager != null;
                List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
                int simCard = getSimSelection(activity);

                // create call intent
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + Uri.encode(number)));

                // add sim selection to call intent
                if (phoneAccountHandleList != null && !phoneAccountHandleList.isEmpty())
                    callIntent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandleList.get(simCard));

                // handle sim card selection
                Timber.d("simcard index %s", simCard);
                if (simCard != -1) callIntent.putExtra("com.android.phone.extra.slot", simCard);

                // start the call
                activity.startActivity(callIntent);

            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Couldn't make a call due to security reasons", Toast.LENGTH_LONG).show();
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Couldnt make a call, no phone number", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity, "Set koler as the default dialer to make calls", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Get sim selection from preferences
     *
     * @param context context
     * @return number of selected sim
     */
    private static int getSimSelection(Context context) {
        return PreferencesManager.getInstance(context).getInt(R.string.pref_sim_select_key, 0);
    }

    /**
     * Call voicemail
     */
    public static boolean callVoicemail(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("voicemail:1")));
            return true;
        } catch (SecurityException e) {
            Toast.makeText(context, "Couldn't start Voicemail", Toast.LENGTH_LONG).show();
            Timber.e(e);
            return false;
        }
    }

    /**
     * Answers incoming call
     */
    public static void answer() {
        if (sCall != null) sCall.answer(VideoProfile.STATE_AUDIO_ONLY);
    }

    /**
     * Ends call
     * If call ended from the other side, disconnects
     */
    public static void reject() {
        if (sCall != null) {
            if (sCall.getState() == Call.STATE_RINGING) sCall.reject(false, null);
            else sCall.disconnect();

            if (sIsAutoCalling) sAutoCallPosition++;
        }
    }

    /**
     * Put call on hold
     *
     * @param isHold put the call on hold or not
     */
    public static void hold(boolean isHold) {
        if (sCall != null) {
            if (isHold) sCall.hold();
            else sCall.unhold();
        }
    }

    /**
     * Open keypad
     *
     * @param c pressed char
     */
    public static void keypad(char c) {
        if (sCall != null) {
            sCall.playDtmfTone(c);
            sCall.stopDtmfTone();
        }
    }

    /**
     * Add a call to the current call
     *
     * @param call to other call to add to the current one
     */
    public static void addCall(Call call) {
        if (sCall != null) sCall.conference(call);
    }

    /**
     * Registers a Callback object to the current call
     *
     * @param callback the callback to register
     */
    public static void registerCallback(Call.Callback callback) {
        if (sCall == null) return;
        sCall.registerCallback(callback);
    }

    /**
     * Unregisters the Callback from the current call
     *
     * @param callback the callback to unregister
     */
    public static void unregisterCallback(Call.Callback callback) {
        if (sCall == null) return;
        sCall.unregisterCallback(callback);
    }

    // -- Getters -- //

    /**
     * Gets the phone number of the contact from the end side of the current call
     * in the case of a voicemail number, returns "Voicemail"
     *
     * @return String - phone number, or voicemail. if not recognized, return null.
     */
    public static Contact getDisplayContact(Context context) {
        String number;

        // try getting the number of the other side of the call
        try {
            number = URLDecoder.decode(sCall.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
        } catch (Exception e) {
            return ContactUtils.UNKNOWN;
        }

        // check if number is a voice mail
        if (number.contains("voicemail")) return ContactUtils.VOICEMAIL;

        // get the contact
        Contact contact = ContactUtils.getContact(context, number, null); // get the contacts with the number
        if (contact == null)
            return new Contact(null, number); // return a number contact
        else return contact; // contact is valid, return it
    }

    /**
     * Returnes the current state of the call from the Call object (named sCall)
     *
     * @return Call.State
     */
    public static int getState() {
        if (sCall == null) return Call.STATE_DISCONNECTED; // if no call, return disconnected
        return sCall.getState();
    }
}
