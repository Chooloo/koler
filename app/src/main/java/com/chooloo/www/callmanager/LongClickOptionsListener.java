package com.chooloo.www.callmanager;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chooloo.www.callmanager.util.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class LongClickOptionsListener implements View.OnTouchListener {

    private static final long LONG_CLICK_MILLIS = 500;
    private static final long ANIMATE_MILLIS = 50;

    private Context mContext;
    private boolean mIsCanceled = false;

    private LongClickRunnable mRunnable = new LongClickRunnable();
    private Handler mHandler = new Handler();
    private int animateChildrenPos = 0;

    private ViewGroup mFabView;
    private List<FloatingActionButton> mFloatingButtons = new ArrayList<>();
    private List<TextView> mActionsText = new ArrayList<>();

    public LongClickOptionsListener(@NotNull Context context, @NotNull ViewGroup fabView) {
        mContext = context;
        this.mFabView = fabView;

        for (int i = 0; i < mFabView.getChildCount(); i++) {
            View v = mFabView.getChildAt(i);
            if (v instanceof FloatingActionButton) {
                mFloatingButtons.add((FloatingActionButton) v);
            }
            if (v instanceof TextView) {
                mActionsText.add((TextView) v);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mIsCanceled = false; //This is no longer canceled

                //Start the timer for long click
                if (!mRunnable.isFinished) {
                    mHandler.postDelayed(mRunnable, LONG_CLICK_MILLIS);
                }
                break;
            case MotionEvent.ACTION_MOVE: //Perform the right action based on pointer's location

                Rect outRect = new Rect();
                v.getDrawingRect(outRect);
                int x = (int) event.getX();
                int y = (int) event.getY();

                //If pointer not inside the button
                if (!outRect.contains(x, y)) {
                    int rawX = (int) event.getRawX();
                    int rawY = (int) event.getRawY();

                    //Cycle through each action button and check if the pointer is ints vicinity
                    for (FloatingActionButton action : mFloatingButtons) {
                        if (Utilities.inViewInBounds(action, rawX, rawY, 8)) {
                            highlightFAB(action, true);
                        } else {
                            highlightFAB(action, false);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();

                boolean actionPerformed = false;
                //Cycle through each action button and check if the pointer is ints vicinity
                for (FloatingActionButton action : mFloatingButtons) {
                    if (Utilities.inViewInBounds(action, rawX, rawY, 16)) {
                        action.performClick();
                        actionPerformed = true;
                    }
                }

                if (!actionPerformed) {
                    if (!mRunnable.isFinished) { //Perform a normal click if this wasn't a long click
                        v.performClick();
                    }
                }

                cancel(); //Don't run code in the long click related runnable
                break;
        }
        return false;
    }

    /**
     * Cancel the long click system
     */
    private void cancel() {
        mIsCanceled = true;
        mRunnable.reset();
        changeVisibility(false);
    }

    /**
     * Change the visibility of the overlay
     *
     * @param overlayVisible whether to show the overlay or not
     */
    private void changeVisibility(boolean overlayVisible) {
        if (overlayVisible) {
            mFabView.animate().alpha(1.0f);
            animateChildrenPos = 0;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (animateChildrenPos >= mFabView.getChildCount()) return;
                    View v = mFabView.getChildAt(animateChildrenPos);
                    if (v instanceof FloatingActionButton) {
                        ((FloatingActionButton) v).show();
                        mHandler.postDelayed(this, ANIMATE_MILLIS);
                    } else {
                        mHandler.post(this);
                    }
                    animateChildrenPos++;
                }
            });
        } else {
            mFabView.animate().alpha(0.0f);
            for (FloatingActionButton actionButton : mFloatingButtons) {
                actionButton.hide();
                actionButton.setHovered(false);
            }
            for (TextView textView : mActionsText) {
                textView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Highlight or unhighlight a given button
     *
     * @param actionButton the button to highlight or unhighlight
     * @param highlight whether to highlight
     */
    private void highlightFAB(FloatingActionButton actionButton, boolean highlight) {
        actionButton.setHovered(highlight);

        //Find the TextView correlated with this button
        int actionIndex = mFloatingButtons.indexOf(actionButton);
        if (actionIndex == -1 || mActionsText.size() <= actionIndex)
            Timber.w("Couldn't find the TextView correlated with action button in index %d", actionIndex);
        TextView actionText = mActionsText.get(actionIndex);
        if (highlight)
            actionText.setVisibility(View.VISIBLE);
        else
            actionText.setVisibility(View.INVISIBLE);
    }

    class LongClickRunnable implements Runnable {

        private boolean isFinished = false;

        void reset() {
            isFinished = false;
        }

        @Override
        public void run() {
            if (!mIsCanceled) {
                changeVisibility(true);
                isFinished = true;
                Utilities.vibrate(mContext);
            }
        }
    }
}
