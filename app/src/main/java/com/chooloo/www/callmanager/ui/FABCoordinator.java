package com.chooloo.www.callmanager.ui;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FABCoordinator {

    private FloatingActionButton mRightFAB;
    private FloatingActionButton mLeftFAB;
    private OnFABClickListener mListener;

    public FABCoordinator(FloatingActionButton rightFAB, FloatingActionButton leftFAB) {
        mRightFAB = rightFAB;
        mLeftFAB = leftFAB;
    }

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

    public void performRightClick() {
        if (mListener != null) mListener.onRightClick();
    }

    public void performLeftClick() {
        if (mListener != null) mListener.onLeftClick();
    }

    public interface OnFABClickListener {
        void onRightClick();
        void onLeftClick();
    }

    public interface FABDrawableCoordination {
        @DrawableRes
        int[] getIconsResources();
    }
}
