package com.chooloo.www.callmanager.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chooloo.www.callmanager.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * A dialog to display the latest features and improvements added to the app.
 * Copied from <a href="https://github.com/kabouzeid/Phonograph"</a>
 */
//TODO adapt to theme
public class ChangelogDialog extends DialogFragment {

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View customView;

        try {
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_changelog, null);
        } catch (InflateException e) {
            e.printStackTrace();
            return new MaterialDialog.Builder(getContext())
                    .title(android.R.string.dialog_alert_title)
                    .content("This device doesn't support web view, which is necessary to view the change log. It is missing a system component.")
                    .positiveText(android.R.string.ok)
                    .build();
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.dialog_changelog_title)
                .customView(customView, true)
                .positiveText(android.R.string.ok)
                .build();

        final WebView webView = customView.findViewById(R.id.web_view);
        try {
            // Load from change_log.html in the assets folder
            StringBuilder buf = new StringBuilder();
            InputStream html = getActivity().getAssets().open("changelog.html");
            BufferedReader in = new BufferedReader(new InputStreamReader(html, StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null)
                buf.append(str);
            in.close();

            //Get theme attributes
            TypedValue background = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.dialogBackgroundColor, background, true);

            TypedValue textColorPrimaryAttr = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, textColorPrimaryAttr, true);

            int colorRes = textColorPrimaryAttr.resourceId != 0 ? textColorPrimaryAttr.resourceId : textColorPrimaryAttr.data;
            int textColorPrimary = ContextCompat.getColor(getContext(), colorRes);

            final String backgroundColor = colorToHex(background.data);
            final String textColor = colorToHex(textColorPrimary);

            //Load the html
            webView.loadData(buf.toString()
                            .replace("{#background-color}", backgroundColor)
                            .replace("{#text-color}", textColor)
                    , "text/html", "UTF-8");
        } catch (Throwable e) {
            webView.loadData("<h1>Unable to load</h1><p>" + e.getLocalizedMessage() + "</p>", "text/html", "UTF-8");
        }

        return dialog;
    }

    private static String colorToHex(int color) {
        return Integer.toHexString(color).substring(2);
    }
}
