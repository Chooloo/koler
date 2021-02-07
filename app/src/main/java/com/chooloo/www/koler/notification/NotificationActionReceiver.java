package com.chooloo.www.koler.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chooloo.www.koler.service.CallManager;
import com.chooloo.www.koler.ui.call.OngoingCallActivity;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(OngoingCallActivity.ACTION_ANSWER)) {
            // If the user pressed "Answer" from the notification
            CallManager.answer();
        } else if (action.equals(OngoingCallActivity.ACTION_HANGUP)) {
            // If the user pressed "Hang up" from the notification
            CallManager.reject();
        }
    }

}
