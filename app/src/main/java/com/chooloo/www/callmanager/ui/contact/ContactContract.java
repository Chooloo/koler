package com.chooloo.www.callmanager.ui.contact;

import android.app.Activity;

import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseContract;

public class ContactContract {
    public interface View extends BaseContract.View {
        Contact getContact();

        Activity getActivity();

        void setUp();

        void toggleFavIcon(boolean toggle);

        void setContactFromIntent();

        void checkNoRecents();

        void onBackPressed();
    }

    public interface Presenter<V extends View> extends BaseContract.Presenter<V> {
        void setActivity();

        void onBackButtonPressed();

        void onActionCall();

        void onActionSms();

        void onActionEdit();

        void onActionInfo();

        void onActionDelete();

        void onActionFav();
    }
}
