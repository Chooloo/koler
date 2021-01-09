package com.chooloo.www.callmanager.ui.call;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.telecom.Call;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.notification.NotificationActionReceiver;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.dialpad.DialpadBottomDialogFragment;
import com.chooloo.www.callmanager.ui.helpers.AllPurposeTouchListener;
import com.chooloo.www.callmanager.ui.helpers.LongClickOptionsListener;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.PreferencesManager;
import com.chooloo.www.callmanager.util.Stopwatch;
import com.chooloo.www.callmanager.util.ThemeUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.chooloo.www.callmanager.util.BiometricUtils.showBiometricPrompt;

@SuppressLint("ClickableViewAccessibility")
//TODO Fix the buttons
public class OngoingCallActivity extends BaseActivity implements DialpadBottomDialogFragment.OnKeyDownListener {

    public static final String ACTION_ANSWER = "ANSWER";
    public static final String ACTION_HANGUP = "HANGUP";
    // Finals
    private static final long END_CALL_MILLIS = 1500;
    private static final String CHANNEL_ID = "notification";
    private static final int NOTIFICATION_ID = 42069;

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

    // Edit Texts


    @Nullable
    ViewGroup mCurrentOverlay = null;
    // Notification
    private boolean mNotificationEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        createNotificationChannel();
        createNotification();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelNotification();
//        this.startService(new Intent(this, RecordService.class)
//                .putExtra("commandType", RECORD_SERVICE_STOP));
    }


    private void updateUI(int state) {
        if (mNotificationEnabled) {
            try {
                mBuilder.setContentText(mStateText);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            } catch (NullPointerException e) {
                // Notifications not supported by the device's android version
            }
        }
    }




    // -- Notification -- //
    private void createNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationEnabled = true;
            Contact callerContact = CallManager.getDisplayContact(this);
            String callerName = callerContact.getName();

            Intent touchNotification = new Intent(this, OngoingCallActivity.class);
            touchNotification.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, touchNotification, 0);

            // Answer Button Intent
            Intent answerIntent = new Intent(this, NotificationActionReceiver.class);
            answerIntent.setAction(ACTION_ANSWER);
            answerIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
            PendingIntent answerPendingIntent = PendingIntent.getBroadcast(this, 0, answerIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            // Hangup Button Intent
            Intent hangupIntent = new Intent(this, NotificationActionReceiver.class);
            hangupIntent.setAction(ACTION_HANGUP);
            hangupIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
            PendingIntent hangupPendingIntent = PendingIntent.getBroadcast(this, 1, hangupIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_full_144)
                    .setContentTitle(callerName)
                    .setContentText(mStateText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setColor(ThemeUtils.getAccentColor(this))
                    .setOngoing(true)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1))
                    .setAutoCancel(true);

            // Adding the action buttons
            mBuilder.addAction(R.drawable.ic_call_black_24dp, getString(R.string.action_answer), answerPendingIntent);
            mBuilder.addAction(R.drawable.ic_call_end_black_24dp, getString(R.string.action_hangup), hangupPendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    /**
     * Creates the notification channel
     * Which allows and manages the displaying of the notification
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager = getSystemService(NotificationManager.class);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Removes the notification
     */
    public void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancel(NOTIFICATION_ID);
    }

    /**
     * detect a nav bar and adapt layout accordingly
     */
    private void adaptToNavbar() {
        boolean hasNavBar = Utilities.hasNavBar(this);
        int navBarHeight = Utilities.navBarHeight(this);
        if (hasNavBar) {
            mOngoingCallLayout.setPadding(0, 0, 0, navBarHeight);
        }
    }

}
