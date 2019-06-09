package com.chooloo.www.callmanager.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.chooloo.www.callmanager.R;

import me.aflak.libraries.callback.FingerprintDialogSecureCallback;
import me.aflak.libraries.callback.PasswordCallback;
import me.aflak.libraries.dialog.FingerprintDialog;
import me.aflak.libraries.dialog.PasswordDialog;
import me.aflak.libraries.utils.FingerprintToken;

public class BiometricUtils {

    @TargetApi(Build.VERSION_CODES.P)
    public static void showBiometricPrompt(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isBiometric = sp.getBoolean(context.getString(R.string.pref_is_biometric_key), false);
        if (isBiometric) {
            if (FingerprintDialog.isAvailable(context)) {
                FingerprintDialog.initialize(context)
                        .title(context.getString(R.string.fingerprint_title))
                        .message(context.getString(R.string.fingerprint_message))
                        .callback(new FingerprintCallback(context), "KeyName1")
                        .show();
            }
        }
    }

    public static class FingerprintCallback implements FingerprintDialogSecureCallback, PasswordCallback {

        Context mContext;

        public FingerprintCallback(Context context) {
            mContext = context;
        }

        @Override
        public void onAuthenticationCancel() {
            // Logic when user canceled operation
            System.exit(0);
        }

        @Override
        public void onAuthenticationSucceeded() {
            // Logic when fingerprint is recognized
        }

        @Override
        public void onNewFingerprintEnrolled(FingerprintToken token) {
            // /!\ A new fingerprint was added /!\
            // Prompt a password to verify identity, then :
            // if (password.correct()) {
            //      token.continueAuthentication();
            // }
            // OR
            // Use PasswordDialog to simplify the process
            PasswordDialog.initialize(mContext, token)
                    .title(mContext.getString(R.string.password_title))
                    .message(mContext.getString(R.string.password_message))
                    .callback(this)
                    .passwordType(PasswordDialog.PASSWORD_TYPE_TEXT)
                    .show();
        }

        @Override
        public boolean onPasswordCheck(String password) {
            return password.equals("password");
        }

        @Override
        public void onPasswordCancel() {
            // Logic when user canceled operation
        }

        @Override
        public void onPasswordSucceeded() {
            // Logic when password is correct (new keys have been generated)
        }

    }

}
