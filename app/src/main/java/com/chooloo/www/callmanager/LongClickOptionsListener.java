package com.chooloo.www.callmanager;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.chooloo.www.callmanager.util.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LongClickOptionsListener implements View.OnTouchListener {

    private static final long LONG_CLICK_MILLIS = 500;

    private Context mContext;
    private boolean mIsCanceled = false;

    private LongClickRunnable mRunnable = new LongClickRunnable();
    private Handler mHandler = new Handler();

    private ViewGroup mFabView;
    private List<FloatingActionButton> mFloatingButtons = new ArrayList<>();

    public LongClickOptionsListener(@NotNull Context context, @NotNull ViewGroup fabView) {
        mContext = context;
        this.mFabView = fabView;

        for (int i = 0; i < mFabView.getChildCount(); i++) {
            View v = mFabView.getChildAt(i);
            if (v instanceof FloatingActionButton) {
                mFloatingButtons.add((FloatingActionButton) v);
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
                        if (Utilities.inViewInBounds(action, rawX, rawY, 16)) {
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

    private void cancel() {
        mIsCanceled = true;
        mRunnable.reset();
        changeVisibility(false);
    }

    private void changeVisibility(boolean overlayVisible) {
        //TODO animate the visibility change
        if (overlayVisible) {
            for (FloatingActionButton actionButton : mFloatingButtons) {
                actionButton.show();
            }
        } else {
            for (FloatingActionButton actionButton : mFloatingButtons) {
                actionButton.hide();
                actionButton.setHovered(false);
            }
        }
    }

    private void highlightFAB(FloatingActionButton actionButton, boolean highlight) {
        actionButton.setHovered(highlight);
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
