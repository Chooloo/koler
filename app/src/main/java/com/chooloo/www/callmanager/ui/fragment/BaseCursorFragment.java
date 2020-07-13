package com.chooloo.www.callmanager.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.AbsFastScrollerAdapter;
import com.chooloo.www.callmanager.adapter.listener.OnItemClickListener;
import com.chooloo.www.callmanager.adapter.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.google.FastScroller;
import com.chooloo.www.callmanager.google.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodels.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodels.SharedSearchViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_CALL_LOG;

public class BaseCursorFragment extends AbsRecyclerViewFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        FABCoordinator.FABDrawableCoordination,
        FABCoordinator.OnFABClickListener,
        View.OnScrollChangeListener,
        OnItemClickListener,
        OnItemLongClickListener {

    private static final int LOADER_ID = 1;
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_CONTACT_NAME = "contact_name";

    private SharedDialViewModel mSharedDialViewModel;
    private SharedSearchViewModel mSharedSearchViewModel;
    private LinearLayoutManager mLayoutManager;
    private AbsFastScrollerAdapter mAdapter;
    private String mRequiredPermission;

    @BindView(R.id.fast_scroller) FastScroller mFastScroller;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.enable_permission_btn) Button mEnablePermissionButton;
    @BindView(R.id.item_header) TextView mAnchoredHeader;
    @BindView(R.id.empty_state) View mEmptyState;
    @BindView(R.id.empty_title) TextView mEmptyTitle;
    @BindView(R.id.empty_desc) TextView mEmptyDesc;

    @LayoutRes
    private int mLayoutID;

    BaseCursorFragment(AbsFastScrollerAdapter adapter, String requiredPermission) {
        this.mAdapter = adapter;
        this.mRequiredPermission = requiredPermission;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // dialer View Model
        mSharedDialViewModel = ViewModelProviders.of(getActivity()).get(SharedDialViewModel.class);
        mSharedDialViewModel.getNumber().observe(this, s -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_PHONE_NUMBER, s);
                LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
            }
        });

        // search Bar View Model
        mSharedSearchViewModel = ViewModelProviders.of(getActivity()).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getText().observe(this, t -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_CONTACT_NAME, t);
                LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
            }
        });

        tryRunningLoader();
    }

    @Override
    protected void onFragmentReady() {
        mLayoutManager =
                new LinearLayoutManager(getContext()) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        int itemsShown = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
                        if (mAdapter.getItemCount() > itemsShown) {
                            mFastScroller.setVisibility(View.VISIBLE);
                            mRecyclerView.setOnScrollChangeListener(BaseCursorFragment.this);
                        } else {
                            mFastScroller.setVisibility(View.GONE);
                        }
                    }
                };

        // Recycle View
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mSharedDialViewModel.setIsOutOfFocus(false);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        mSharedDialViewModel.setIsOutOfFocus(true);
                        mSharedSearchViewModel.setIsFocused(false);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        mSharedDialViewModel.setIsOutOfFocus(true);
                        mSharedSearchViewModel.setIsFocused(false);
                    default:
                        mSharedDialViewModel.setIsOutOfFocus(false);
                }
            }
        });

        // Refresh Layout
        mRefreshLayout.setOnRefreshListener(() -> {
            LoaderManager.getInstance(BaseCursorFragment.this).restartLoader(LOADER_ID, null, BaseCursorFragment.this);
            tryRunningLoader();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tryRunningLoader();
    }

    /*
     * When our recycler view updates, we need to ensure that our row headers and anchored header
     * are in the correct state.
     *
     * The general rule is, when the row headers are shown, our anchored header is hidden. When the
     * recycler view is scrolling through a sublist that has more than one element, we want to show
     * out anchored header, to create the illusion that our row header has been anchored. In all
     * other situations, we want to hide the anchor because that means we are transitioning between
     * two sublists.
     */
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        int firstCompletelyVisible = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstCompletelyVisible == RecyclerView.NO_POSITION) {
            // No items are visible, so there are no headers to update.
            return;
        }
        String anchoredHeaderString = mAdapter.getHeaderString(firstCompletelyVisible);

        // If the user swipes to the top of the list very quickly, there is some strange behavior
        // between this method updating headers and adapter#onBindViewHolder updating headers.
        // To overcome this, we refresh the headers to ensure they are correct.
        if (firstVisibleItem == firstCompletelyVisible && firstVisibleItem == 0) {
            mAdapter.refreshHeaders();
            mAnchoredHeader.setVisibility(View.INVISIBLE);
        } else {
            if (mAdapter.getHeaderString(firstVisibleItem).equals(anchoredHeaderString)) {
                mAnchoredHeader.setText(anchoredHeaderString);
                mAnchoredHeader.setVisibility(View.VISIBLE);
                getContactHolder(firstVisibleItem).getHeaderView().setVisibility(View.INVISIBLE);
                getContactHolder(firstCompletelyVisible).getHeaderView().setVisibility(View.INVISIBLE);
            } else {
                mAnchoredHeader.setVisibility(View.INVISIBLE);
                getContactHolder(firstVisibleItem).getHeaderView().setVisibility(View.VISIBLE);
                getContactHolder(firstCompletelyVisible).getHeaderView().setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, Object data) {
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder holder, Object data) {
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String contactName = args != null && args.containsKey(ARG_CONTACT_NAME) ? args.getString(ARG_CONTACT_NAME) : null;
        String phoneNumber = args != null && args.containsKey(ARG_PHONE_NUMBER) ? args.getString(ARG_PHONE_NUMBER) : null;
        return new FavoritesAndContactsLoader(getContext(), phoneNumber, contactName);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        setData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onLeftClick() {
    }

    @Override
    public int[] getIconsResources() {
        return new int[]{};
    }

    // -- Loader -- //

    private void tryRunningLoader() {
        if (!isLoaderRunning() && Utilities.checkPermissionGranted(getContext(), mRequiredPermission, true)) {
            runLoader();
            mEnablePermissionButton.setVisibility(View.GONE);
        }
    }

    private void runLoader() {
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    private boolean isLoaderRunning() {
        Loader loader = LoaderManager.getInstance(this).getLoader(LOADER_ID);
        return loader != null;
    }

    private void setData(Cursor data) {
        mAdapter.changeCursor(data);
        mFastScroller.setup(mAdapter, mLayoutManager);
        if (mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(false);
        if (data != null && data.getCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checking whither to show the "enable contacts" button
     */
    public void checkShowButton() {
        if (Utilities.checkPermissionGranted(getContext(), mRequiredPermission, false)) {
            mEnablePermissionButton.setVisibility(View.GONE);
        } else {
            mEmptyTitle.setText(R.string.empty_recents_persmission_title);
            mEmptyDesc.setText(R.string.empty_recents_persmission_desc);
            mEnablePermissionButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.enable_permission_btn)
    public void askForCallLogPermission() {
        Utilities.askForPermission(getActivity(), mRequiredPermission);
    }
}
