package com.chooloo.www.callmanager.ui.page;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.ui.main.MainActivity;
import com.chooloo.www.callmanager.ui2.fragment.RecentsFragment;
import com.chooloo.www.callmanager.viewmodel.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodel.SharedSearchViewModel;

public class PageFragmentRecents extends BaseFragment implements PageContract.View, MainActivity.FABManager {

    private PagePresenter<PageContract.View> mPresenter;

    private Context mContext;

    private RecentsFragment mRecentsFragment;

    public PageFragmentRecents(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        mPresenter = new PagePresenter();
        mPresenter.bind(this, getLifecycle());
        mPresenter.subscribe(mContext);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_page_layout, mRecentsFragment).commit();
    }

    @Override
    public void onStart() {
        super.onStart();

        setUp();
    }

    @Override
    public void setUp() {
        mRecentsFragment = new RecentsFragment(mContext);

        // dialer view model
        SharedDialViewModel sharedDialViewModel = ViewModelProviders.of(getActivity()).get(SharedDialViewModel.class);
        sharedDialViewModel.getNumber().observe(this, s -> mRecentsFragment.load(s, s == "" ? null : s));

        // search bar view model
        SharedSearchViewModel sharedSearchViewModel = ViewModelProviders.of(getActivity()).get(SharedSearchViewModel.class);
        sharedSearchViewModel.getText().observe(this, t -> mRecentsFragment.load(null, t == "" ? null : t));

        mRecentsFragment.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        sharedDialViewModel.setIsFocused(false);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        sharedDialViewModel.setIsFocused(true);
                        sharedSearchViewModel.setIsFocused(false);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        sharedDialViewModel.setIsFocused(true);
                        sharedSearchViewModel.setIsFocused(false);
                    default:
                        sharedDialViewModel.setIsFocused(false);
                }
            }
        });
    }

    public void loadContacts(String phoneNumber, String contactName) {
        mRecentsFragment.load(phoneNumber, contactName);
    }

    @Override
    public void onLeftClick() {
        mPresenter.onLeftClick();
    }

    @Override
    public void onRightClick() {
        mPresenter.onRightClick();
    }

    @Override
    public int[] getIconsResources() {
        return new int[]{
                R.drawable.ic_dialpad_black_24dp,
                R.drawable.ic_search_black_24dp
        };
    }
}
