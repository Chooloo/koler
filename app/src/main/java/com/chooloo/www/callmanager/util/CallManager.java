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

import androidx.appcompat.app.AppCompatActivity;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.activity.OngoingCallActivity;
import com.chooloo.www.callmanager.util.validation.Validator;

import org.jetbrains.annotations.NotNull;

import java.net.URLDecoder;
import java.security.Permission;
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
                Timber.d("simcard %s", simCard);
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
        try {
            return Integer.parseInt(String.valueOf(PreferenceUtils.getInstance(context).getString(R.string.pref_sim_select_key)));
        } catch (NullPointerException e) {
            return -1;
        }
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
    public static void registerCallback(OngoingCallActivity.Callback callback) {
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

    // -- Auto-Calling -- //

    /**
     * Start the auto calling on the list of contacts
     *
     * @param list          a list of contacts
     * @param context       the context
     * @param startPosition the start position from which to start calling
     */
    public static void startAutoCalling(@NotNull List<Contact> list, @NotNull AppCompatActivity context, int startPosition) {
        sIsAutoCalling = true;
        sAutoCallPosition = startPosition;
        sAutoCallingContactsList = list;
        if (sAutoCallingContactsList.isEmpty()) Timber.e("No contacts in auto calling list");
        nextCall(context);
    }

    /**
     * Go to the next call
     *
     * @param activity activity
     */
    public static void nextCall(@NotNull Activity activity) {
        if (sAutoCallingContactsList != null && sAutoCallPosition < sAutoCallingContactsList.size()) {
            String phoneNumber = sAutoCallingContactsList.get(sAutoCallPosition).getMainPhoneNumber();
            if (Validator.validatePhoneNumber(phoneNumber)) {
                call(activity, phoneNumber);
            } else {
                Toast.makeText(activity, "Can't call " + phoneNumber, Toast.LENGTH_SHORT).show();
                sAutoCallPosition++;
                nextCall(activity);
            }
        } else {
            finishAutoCall();
        }
    }

    /**
     * Finish the loop
     */
    private static void finishAutoCall() {
        sIsAutoCalling = false;
        sAutoCallPosition = 0;
    }

    /**
     * Check wither is currently auto calling
     */
    public static boolean isAutoCalling() {
        return sIsAutoCalling;
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
        if (contact == null) return new Contact(number, number, null); // return a number contact
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
