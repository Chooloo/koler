package com.chooloo.www.callmanager.ui;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FABCoordinator {

    private FloatingActionButton mRightFAB;
    private FloatingActionButton mLeftFAB;
    private OnFABClickListener mListener;
    private Context mContext;

    /**
     * Constructor
     *
     * @param rightFAB right action button on the screen
     * @param leftFAB  left action button on the screen
     */
    public FABCoordinator(FloatingActionButton rightFAB, FloatingActionButton leftFAB, Context context) {
        mRightFAB = rightFAB;
        mLeftFAB = leftFAB;
        mContext = context;
    }

    /**
     * Sets the listener by a given fragment
     * (the given fragment needs to be an instance of the OnFABClickListener)
     *
     * @param fragment
     */
    public void setListener(Fragment fragment) {
        if (!(fragment instanceof OnFABClickListener) || !(fragment instanceof FABDrawableCoordination)) {
            mListener = null;
            mRightFAB.setEnabled(false);
            mLeftFAB.setEnabled(false);
            return;
        }

        //Set the correct icons
        int[] resources = ((FABDrawableCoordination) fragment).getIconsResources();
        if (resources[0] != -1) {
            mRightFAB.setEnabled(true);
            mRightFAB.setImageResource(resources[0]);
        } else {
            mRightFAB.setEnabled(false);
        }

        if (resources[1] != -1) {
            mLeftFAB.setEnabled(true);
            mLeftFAB.setImageResource(resources[1]);
        } else {
            mLeftFAB.setEnabled(false);
        }

        //Set the listener;
        mListener = (OnFABClickListener) fragment;

    }

    /**
     * Clicks on the right FAB
     */
    public void performRightClick() {
        if (mListener != null) mListener.onRightClick();
    }

    /**
     * Clicks on the left FAB
     */
    public void performLeftClick() {
        if (mListener != null) mListener.onLeftClick();
    }

    /**
     * OnFABClickListener to implement in the target fragment
     */
    public interface OnFABClickListener {
        void onRightClick();

        void onLeftClick();
    }

    /**
     * FABDrawableCoordination
     * Gets the required resources for the FABs
     */
    public interface FABDrawableCoordination {
        @DrawableRes
        int[] getIconsResources();
    }
}
