package com.chooloo.www.callmanager.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.chooloo.www.callmanager.BuildConfig;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.dialog.ChangelogDialog;
import com.chooloo.www.callmanager.ui.widgets.ListItem;
import com.chooloo.www.callmanager.util.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AbsThemeActivity {

    private static final String TAG_CHANGELOG_DIALOG = "changelog";

    @BindView(R.id.about_version) ListItem mVersionItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVersionItem.setDescription("" + BuildConfig.VERSION_NAME);
    }

    @OnClick(R.id.about_changelog)
    public void openChangelog(View v) {
        new ChangelogDialog().show(getSupportFragmentManager(), TAG_CHANGELOG_DIALOG);
    }

    @OnClick(R.id.about_source)
    public void openSource(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_source_url)));
        startActivity(intent);
    }

    @OnClick(R.id.about_email)
    public void sendEmail(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.support_email)});
        intent.setType("message/rfc822");
        startActivity(intent);
    }

    @OnClick(R.id.about_bugs)
    public void openBugReporting(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_bug_reporting_url)));
        startActivity(intent);
    }

    @OnClick(R.id.about_rate)
    public void openRatingPage(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getApplication().getPackageName()));
        startActivity(intent);
    }

    @OnClick(R.id.about_donate)
    public void openDonation(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.donation_link)));
        startActivity(intent);
    }
}