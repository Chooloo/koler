package com.chooloo.www.callmanager.ui.dialpad;

public class DialpadContract implements BaseContract {
    interface View extends BaseContract.View {
        void setUp();

        boolean isDialer();

        void setNumber(String number);

        void setViewModelNumber(String number);

        void call();

        void callVoicemail();

        void requestFocus();

        void toggleToneGenerator(boolean toggle);

        void stopTone();

        void playTone(int keyCode);

        void toggleCursor(boolean isShow);

        void registerKeyEvent(int keyCode);

        void vibrate();

        void setDigitsCanBeEdited(boolean isCanBeEdited);

        void showVoicemailButton(boolean isShow);
    }

    interface Presenter<V extends View> extends BaseContract.Presenter<V> {
        void onResume();

        void onPause();

        void onKeyClick(int keyCode);

        void onCallClick();

        void onDigitsClick();

        void onLongDeleteClick();

        void onLongOneClick();

        void onLongZeroClick();

        void onHiddenChanged(boolean hidden);

        void onIntentDataChanged(String data);

        void onTextChanged(String text);

    }
}
