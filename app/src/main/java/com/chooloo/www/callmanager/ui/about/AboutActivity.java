package com.chooloo.www.callmanager.ui.about;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.BuildConfig;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.ActivityAboutBinding;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.dialog.ChangelogDialog;

public class AboutActivity extends BaseActivity implements AboutMvpView {

    private static final String TAG_CHANGELOG_DIALOG = "changelog";

    private AboutPresenter<AboutMvpView> mPresenter;
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public void setUp() {
        mPresenter = new AboutPresenter<>();
        mPresenter.onAttach(this);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        binding.cardGeneral.aboutVersion.setSmallText("" + BuildConfig.VERSION_NAME);

        binding.cardGeneral.aboutChangelog.setOnClickListener(view -> mPresenter.onChangelogClick());
        binding.cardGeneral.aboutSource.setOnClickListener(view -> mPresenter.onOpenSourceClick());
        binding.cardDeveloper.aboutEmail.setOnClickListener(view -> mPresenter.onSendEmailClick());
        binding.cardSupport.aboutBugs.setOnClickListener(view -> mPresenter.onReportBugClick());
        binding.cardSupport.aboutRate.setOnClickListener(view -> mPresenter.onRateAppClick());
        binding.cardSupport.aboutDonate.setOnClickListener(view -> mPresenter.onDonateClick());
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
