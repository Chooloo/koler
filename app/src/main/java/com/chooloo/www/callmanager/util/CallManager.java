package com.chooloo.www.callmanager.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.VideoProfile;
import android.telephony.SubscriptionManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.activity.OngoingCallActivity;
import com.chooloo.www.callmanager.util.validation.Validator;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Permission;
import java.util.List;

import timber.log.Timber;

import static android.Manifest.permission.CALL_PHONE;

public class CallManager {

    // Variables
    public static Call sCall;
    private static boolean sIsAutoCalling = false;
    private static List<Contact> sAutoCallingContactsList = null;
    private static int sAutoCallPosition = 0;
    private static SubscriptionManager mSubscriptionManager;

    // -- Call Actions -- //

    /**
     * Call a given number
     *
     * @param context
     * @param number
     */
    public static void call(@NotNull Context context, @NotNull String number) {
        try {
            // Create call intent
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + Uri.encode(number)));
            int simCard = getSimSelection(context); // handle sim card selection
            if (simCard != -1) callIntent.putExtra("com.android.phone.extra.slot", simCard);
            context.startActivity(callIntent); // start the call
        } catch (SecurityException e) {
            Toast.makeText(context, "Couldn't make a call due to security reasons", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            Toast.makeText(context, "Couldnt make a call, no phone number", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Get sim selection from preferences
     *
     * @param context
     * @return number of selected sim
     */
    public static int getSimSelection(Context context) {
        try {
            return PreferenceUtils.getInstance(context).getInt(R.string.pref_sim_select_key);
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
     *
     * @return true whether there's no more calls awaiting
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
     * @param hold
     */
    public static void hold(boolean hold) {
        if (sCall != null) {
            if (hold) sCall.hold();
            else sCall.unhold();
        }
    }

    /**
     * Open keypad
     *
     * @param c
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
     * @param call
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
     * @param context
     */
    public static void nextCall(@NotNull Context context) {
        if (sAutoCallingContactsList != null && sAutoCallPosition < sAutoCallingContactsList.size()) {
            String phoneNumber = sAutoCallingContactsList.get(sAutoCallPosition).getMainPhoneNumber();
            if (Validator.validatePhoneNumber(phoneNumber)) {
                call(context, phoneNumber);
            } else {
                Toast.makeText(context, "Can't call " + phoneNumber, Toast.LENGTH_SHORT).show();
                sAutoCallPosition++;
                nextCall(context);
            }
        } else {
            finishAutoCall();
        }
    }

    /**
     * Finish the loop
     */
    public static void finishAutoCall() {
        sIsAutoCalling = false;
        sAutoCallPosition = 0;
    }

    /**
     * Check wither is currently auto calling
     *
     * @return
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
