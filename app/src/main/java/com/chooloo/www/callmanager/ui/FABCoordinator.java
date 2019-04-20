package com.chooloo.www.callmanager.ui;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.DrawableRes;

public class FABCoordinator {

    private FloatingActionButton mRightFAB;
    private FloatingActionButton mLeftFAB;
    private OnFabClickListener mListener;

    public FABCoordinator(FloatingActionButton rightFAB, FloatingActionButton leftFAB) {
        mRightFAB = rightFAB;
        mLeftFAB = leftFAB;
    }

    public void setListener(OnFabClickListener listener) {
        mListener = listener;
        int[] resources = mListener.getIconsResources();

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
    }

    public void performRightClick() {
        if (mListener != null) mListener.onRightClick();
    }

    public void performLeftClick() {
        if (mListener != null) mListener.onLeftClick();
    }

    public interface OnFabClickListener {
        @DrawableRes
        int[] getIconsResources();

        void onRightClick();

        void onLeftClick();
    }
}
