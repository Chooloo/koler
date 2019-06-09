package com.chooloo.www.callmanager.listener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class AllPurposeTouchListener implements View.OnTouchListener {

    // Constants
    private final GestureDetector mGestureDetector;
    private final GestureListener mGestureListener;

    /**
     * Constructor
     *
     * @param ctx
     */
    public AllPurposeTouchListener(Context ctx) {
        mGestureListener = new GestureListener();
        mGestureDetector = new GestureDetector(ctx, mGestureListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureListener.setView(v);
        return mGestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        // Constants
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        private View mView;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return AllPurposeTouchListener.this.onSingleTapConfirmed(mView);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return AllPurposeTouchListener.this.onSingleTapUp(mView);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            AllPurposeTouchListener.this.onLongPress(mView);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            boolean result = false;

            try {

                // The difference in the Y position
                float diffY = e2.getY() - e1.getY();
                // The difference in the X position
                float diffX = e2.getX() - e1.getX();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    // The fling is horizontal
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        // The fling is actually a fling
                        if (diffX > 0) {
                            // The fling is to the right (the difference in the position is positive)
                            onSwipeRight();
                        } else {
                            // The fling is to the left (the difference in the position is negative)
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    // The fling is vertical and is actually a fling
                    if (diffY > 0) {
                        // The fling is downwards
                        onSwipeBottom();
                    } else {
                        // The fling is upwards
                        onSwipeTop();
                    }
                    result = true;
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return result;

        }

        public void setView(View view) {
            mView = view;
        }
    }

    /**
     * Notified when a single-tap occurs.
     * <p>
     * Unlike {@link #onSingleTapUp(View)}, this
     * will only be called after the detector is confident that the user's
     * first tap is not followed by a second tap leading to a double-tap
     * gesture.
     *
     * @param v The view the tap occurred on.
     */
    public boolean onSingleTapConfirmed(View v) {
        return false;
    }

    /**
     * Notified when a tap occurs.
     *
     * @param v The view the tap occurred on.
     */
    public boolean onSingleTapUp(View v) {
        return false;
    }

    public void onLongPress(View v) {
    }

    /**
     * If the user swipes right
     */
    public void onSwipeRight() {
    }

    /**
     * If the user swipes left
     */
    public void onSwipeLeft() {
    }

    /**
     * If the user swipes up
     */
    public void onSwipeTop() {
    }

    /**
     * Guess what? if the user swipes down
     */
    public void onSwipeBottom() {
    }
}
