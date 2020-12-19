package com.chooloo.www.callmanager.ui.contacts;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.cursor.CursorFragment;
import com.chooloo.www.callmanager.ui.helpers.ListItemHolder;
import com.chooloo.www.callmanager.ui.widgets.FastScroller;

import butterknife.BindView;

import static android.Manifest.permission.READ_CONTACTS;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ContactsFragment extends CursorFragment<ContactsAdapter<ListItemHolder>> implements ContactsMvpView {

    private static final String[] REQUIRED_PERMISSIONS = {READ_CONTACTS};
    private final static String ARG_PHONE_NUMBER = "phoneNumber";
    private final static String ARG_CONTACT_NAME = "contactName";

    private ContactsPresenter<ContactsMvpView> mPresenter;
    private LinearLayoutManager mLayoutManager;

    public static ContactsFragment newInstance() {
        return ContactsFragment.newInstance(null, null);
    }

    public static ContactsFragment newInstance(@Nullable String phoneNumber, @Nullable String contactNumber) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public ContactsAdapter<ListItemHolder> getAdapter() {
        ContactsAdapter<ListItemHolder> contactsAdapter = new ContactsAdapter<>(mActivity);
        contactsAdapter.setOnContactItemClick(contact -> mPresenter.onContactItemClick(contact));
        contactsAdapter.setOnContactItemLongClickListener(contact -> mPresenter.onContactItemLongClick(contact));
        return contactsAdapter;
    }

    @Override
    public String[] getPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    public Loader<Cursor> getLoader(Bundle args) {
        String contactName = args != null ? args.getString(ARG_CONTACT_NAME, null) : null;
        String phoneNumber = args != null ? args.getString(ARG_PHONE_NUMBER, null) : null;
        return new FavoritesAndContactsLoader(mActivity, phoneNumber, contactName);
    }

    @Override
    public void setUp() {
        super.setUp();

        mPresenter = new ContactsPresenter<>();
        mPresenter.onAttach(this);

        mLayoutManager = new LinearLayoutManager(mActivity) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                int itemsShown = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
                binding.fastScroller.setVisibility(getSize() > itemsShown ? VISIBLE : GONE);
            }
        };
        binding.recyclerView.setLayoutManager(mLayoutManager);

        binding.fastScroller.setFastScrollerHeaderManager(new FastScroller.FastScrollerHeaderManager() {
            @Override
            public String getHeaderString(int position) {
                return mAdapter.getHeader(position);
            }

            @Override
            public void refreshHeaders() {
                mPresenter.onRefreshHeaders();
            }
        });

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mPresenter.onScrolled();
            }
        });

        load();
    }

    @Override
    public void load(@Nullable String phoneNumber, @Nullable String contactName) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactName);
        setArguments(args);
        load();
    }

    @Override
    public String getHeader(int position) {
        return mAdapter.getHeader(position);
    }

    @Override
    public ListItemHolder getContactHolder(int position) {
        return ((ListItemHolder) binding.recyclerView.findViewHolderForAdapterPosition(position));
    }

    @Override
    public void openContact(Contact contact) {
        // TODO implement
    }

    @Override
    public void refreshHeaders() {
        mAdapter.refreshHeaders();
    }

    @Override
    public void updateScroll() {
        binding.fastScroller.updateContainerAndScrollBarPosition(binding.recyclerView);
        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
        int firstCompletelyVisible = mLayoutManager.findFirstCompletelyVisibleItemPosition();

        // No items are visible, so there are no headers to update.
        if (firstCompletelyVisible == RecyclerView.NO_POSITION) {
            return;
        }

        String anchoredHeaderString = mAdapter.getHeader(firstCompletelyVisible);

        // If the user swipes to the top of the list very quickly, there is some strange behavior
        // between this method updating headers and adapter#onBindViewHolder updating headers.
        // To overcome this, we refresh the headers to ensure they are correct.
        if (firstVisibleItem == firstCompletelyVisible && firstVisibleItem == 0) {
            mAdapter.refreshHeaders();
            binding.listItemHeader.listItemHeader.setVisibility(INVISIBLE);
        } else {
            boolean headerIsAnchored = mAdapter.getHeader(firstVisibleItem).equals(anchoredHeaderString);
            binding.listItemHeader.listItemHeader.setVisibility(headerIsAnchored ? VISIBLE : INVISIBLE);
            getContactHolder(firstVisibleItem).getListItem().showHeader(!headerIsAnchored);
            getContactHolder(firstCompletelyVisible).getListItem().showHeader(!headerIsAnchored);
            if (headerIsAnchored) {
                binding.listItemHeader.listItemHeader.setText(anchoredHeaderString);
            }
        }
    }

    @Override
    public void setupFastScroller() {
        binding.fastScroller.setup(mAdapter, mLayoutManager);
    }
}
