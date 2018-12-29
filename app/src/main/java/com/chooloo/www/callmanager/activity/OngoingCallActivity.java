package com.chooloo.www.callmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.service.CallManager;
import com.chooloo.www.callmanager.service.CallService;

public class OngoingCallActivity extends AppCompatActivity {

    @BindView(R.id.answerBtn) Button answerBtn;
    @BindView(R.id.denyBtn) Button denyBtn;
    @BindView(R.id.placeholder) TextView statusTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);
        ButterKnife.bind(this);

        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallManager.acceptCall();
                statusTxt.setText("Call In Progress");
            }
        });

        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallManager.denyCall();
                statusTxt.setText("Call Ended");
            }
        });

    }
}
