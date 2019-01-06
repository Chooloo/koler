package com.chooloo.www.callmanager.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OngoingCallActivity extends AppCompatActivity {

    @BindView(R.id.ongoingcall_layout)
    ConstraintLayout mParentLayout;
    @BindView(R.id.answer_btn)
    FloatingActionButton mAnswerButton;
    @BindView(R.id.deny_btn)
    FloatingActionButton mDenyButton;
    @BindView(R.id.text_status)
    TextView mStatusText;
    @BindView(R.id.caller_text)
    TextView mCallerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_call);

        //This activity needs to show even if the screen is off or locked
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (km != null) {
                km.requestDismissKeyguard(this, null);
            }
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }

        ButterKnife.bind(this);

        // Set the caller name text view
        mCallerText.setText(CallManager.getPhoneNumber());

        // Get the caller's contact name
        String contactName = CallManager.getContactName(this);
        if (contactName != null) {
            mCallerText.setText(contactName);
        }
    }

    @OnClick(R.id.answer_btn)
    public void answer(View view) {
        CallManager.sAnswer();
        mStatusText.setText(getResources().getString(R.string.status_ongoing_call));
        mParentLayout.setBackgroundColor(getResources().getColor(R.color.call_in_progress_background));
    }

    @OnClick(R.id.deny_btn)
    public void deny(View view) {
        mParentLayout.setBackgroundColor(getResources().getColor(R.color.call_ended_background));
        mStatusText.setText(getResources().getString(R.string.status_call_ended));
        CallManager.sReject();
        finish();
    }

    //TODO Change the UI depending on the state (active/calling/hold...)
    private void updateUI(int state) {
        switch (state) {
            case 2:
                mStatusText.setText(getResources().getString(R.string.status_call_incoming));
                break;
            case 1:
                mStatusText.setText(getResources().getString(R.string.status_call_dialing));
                break;
            case 3:
                mStatusText.setText(getResources().getString(R.string.status_call_holding));
                break;
            case 4:
                mStatusText.setText(getResources().getString(R.string.status_call_actuve));
                break;
        }
    }

    public class Callback extends Call.Callback {

        private Context context;

        public Callback(Context context) {
            this.context = context;
        }

        @Override
        public void onStateChanged(Call call, int state) {
//        int = Call.State
//        1   = Call.STATE_DIALING
//        2   = Call.STATE_RINGING
//        3   = Call.STATE_HOLDING
//        4   = Call.STATE_ACTIVE
//        7   = Call.STATE_DISCONNECTED
//        8   = Call.STATE_SELECT_PHONE_ACCOUNT
//        9   = Call.STATE_CONNECTING
//        10  = Call.STATE_DISCONNECTING
//        11  = Call.STATE_PULLING_CALL
            super.onStateChanged(call, state);
            String stringState = String.valueOf(state);
            Log.i("StateChanged", stringState);
            updateUI(state);
        }

        @Override
        public void onDetailsChanged(Call call, Call.Details details) {
            super.onDetailsChanged(call, details);
            Log.i("DetailesChanged", details.toString());
        }


    }
}
