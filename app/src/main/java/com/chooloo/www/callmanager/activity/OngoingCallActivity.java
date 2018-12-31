package com.chooloo.www.callmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.service.CallManager;
import com.chooloo.www.callmanager.service.CallService;

public class OngoingCallActivity extends AppCompatActivity {
    // TODO 1 make a list of mSTATUS options
    // TODO 2 try to make a click listener when the call has ended to close the app
    @BindView(R.id.answerBtn)
    Button answerBtn;
    @BindView(R.id.denyBtn)
    Button denyBtn;
    @BindView(R.id.placeholder)
    TextView statusTxt;

    static String mSTATUS = "INCOMING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);
        ButterKnife.bind(this);

        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            statusTxt.setText(telecomManager.getLine1Number(CallManager.mCall.getDetails().getAccountHandle()));
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }

        View.OnTouchListener Clicked = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mSTATUS == "ENDED") {
                    System.exit(0);
                }
                return true;
            }
        };

        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call.Details details = CallManager.acceptCall();
                Log.e("caller details", details.getCallerDisplayName());
                statusTxt.setText("Call In Progress");
                mSTATUS = "PROGRESS";
            }
        });

        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallManager.denyCall();
                statusTxt.setText("Call Ended");
                mSTATUS = "ENDED";
            }
        });

    }
}
