package com.chooloo.www.callmanager.activity;

import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.service.CallManager;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OngoingCallActivity extends AppCompatActivity {
    // TODO 1 make a list of mSTATUS options
    // TODO 2 try to make a click listener when the call has ended to close the app
    @BindView(R.id.answerBtn) Button answerBtn;
    @BindView(R.id.denyBtn) Button denyBtn;
    @BindView(R.id.placeholder) TextView statusTxt;

    static String mSTATUS = "INCOMING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);
        ButterKnife.bind(this);

        answerBtn.setOnClickListener(v -> {
            Call.Details details = CallManager.acceptCall();
            Log.e("caller details", details.getCallerDisplayName());
            statusTxt.setText("Call In Progress");
            mSTATUS = "PROGRESS";
        });

        denyBtn.setOnClickListener(v -> {
            CallManager.denyCall();
            statusTxt.setText("Call Ended");
            mSTATUS = "ENDED";
        });

    }
}
