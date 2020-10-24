package com.chooloo.www.callmanager.ui.activity.contact;

import com.chooloo.www.callmanager.ui.base.BaseContract;

public class ContactContract {
    public interface View extends BaseContract.View {

        void setUp();

        void actionCall();

        void actionSms();

        void actionEdit();

        void actionInfo();

        void actionDelete();

        void actionFav();

        void toggleFavIcon(boolean toggle);

        void setContactFromIntent();

        void handleNoRecents();

        void onBackPressed();
    }

    public interface Presenter<V extends View> extends BaseContract.Presenter<V> {

        void onBackButtonPressed();

        void onActionCall();

        void onActionSms();

        void onActionEdit();

        void onActionInfo();

        void onActionDelete();

        void onActionFav();
    }
}
