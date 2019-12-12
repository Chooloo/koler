package com.chooloo.www.callmanager.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.widget.Toast;

import com.chooloo.www.callmanager.ui.activity.OngoingCallActivity;
import com.chooloo.www.callmanager.util.CallManager;

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
