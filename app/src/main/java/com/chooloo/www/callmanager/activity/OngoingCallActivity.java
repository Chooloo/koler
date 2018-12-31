package com.chooloo.www.callmanager.activity;

import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.CallManager;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

// TODO make a list of mSTATUS options
// TODO try to make a click listener when the call has ended to close the app
public class OngoingCallActivity extends AppCompatActivity {
    @BindView(R.id.answer_button) Button mAnswerButton;
    @BindView(R.id.button_deny) Button mDenyButton;
    @BindView(R.id.text_status) TextView mStatusText;

    static String mSTATUS = "INCOMING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.answer_button)
    public void answer(View view) {
        Call.Details details = CallManager.acceptCall();
        Timber.e(details.getCallerDisplayName());
        mStatusText.setText("Call In Progress");
        mSTATUS = "PROGRESS";
    }

    @OnClick(R.id.button_deny)
    public void deny(View view) {
        CallManager.denyCall();
        mStatusText.setText("Call Ended");
        mSTATUS = "ENDED";
    }
}
