package com.chooloo.www.callmanager.ui2;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.database.entity.Contact;

public class ContactItemHolder extends ListItemHolder {
    private Contact mContact;

    public ContactItemHolder(@NonNull View itemView, Contact contact) {
        super(itemView);

    }
}
