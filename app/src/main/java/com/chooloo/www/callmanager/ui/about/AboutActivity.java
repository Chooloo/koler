package com.chooloo.www.callmanager.ui.about;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.BuildConfig;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseThemeActivity;
import com.chooloo.www.callmanager.ui.dialog.ChangelogDialog;
import com.chooloo.www.callmanager.ui.widgets.ListItem;
import com.chooloo.www.callmanager.util.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseThemeActivity implements AboutMvpView {

    private static final String TAG_CHANGELOG_DIALOG = "changelog";

    private AboutPresenter<AboutMvpView> mPresenter;

    @BindView(R.id.about_version) ListItem mVersionItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        mPresenter = new AboutPresenter();
        mPresenter.onAttach(this, getLifecycle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public void setUp() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mVersionItem.setDescription("" + BuildConfig.VERSION_NAME);
    }

    @OnClick(R.id.about_changelog)
    public void changelogClick(View v) {
        mPresenter.onChangelogClick();
    }

    @OnClick(R.id.about_source)
    public void openSourceClick(View v) {
        mPresenter.onOpenSourceClick();
    }

    @OnClick(R.id.about_email)
    public void sendEmailClick(View v) {
        mPresenter.onSendEmailClick();
    }

    @OnClick(R.id.about_bugs)
    public void reportBugClick(View v) {
        mPresenter.onReportBugClick();
    }

    @OnClick(R.id.about_rate)
    public void rateAppClick(View v) {
        mPresenter.onRateAppClick();
    }

    @OnClick(R.id.about_donate)
    public void donateClick(View v) {
        mPresenter.onDonateClick();
    }

    @Override
    public void openChangelog() {
        new ChangelogDialog().show(getSupportFragmentManager(), TAG_CHANGELOG_DIALOG);
    }

    @Override
    public void openSource() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_source_url)));
        startActivity(intent);
    }

    @Override
    public void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.support_email)});
        intent.setType("message/rfc822");
        startActivity(intent);
    }

    @Override
    public void reportBug() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_bug_reporting_url)));
        startActivity(intent);
    }

    @Override
    public void rateApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getApplication().getPackageName()));
        startActivity(intent);
    }

    @Override
    public void donate() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.donation_link)));
        startActivity(intent);
    }
}
