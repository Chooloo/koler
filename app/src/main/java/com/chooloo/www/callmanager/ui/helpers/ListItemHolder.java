package com.chooloo.www.callmanager.ui.helpers;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;

public class ListItemHolder extends RecyclerView.ViewHolder {

    private OnItemClickListener mOnItemClickListener;

    @BindView(R.id.item_photo_placeholder) ImageView mPhotoPlaceholder;
    @BindView(R.id.item_photo) ImageView mPhoto;
    @BindView(R.id.item_big_text) TextView mBigText;
    @BindView(R.id.item_small_text) TextView mSmallText;
    @BindView(R.id.item_header) TextView mHeader;

    /**
     * Constructor
     *
     * @param itemView the layout view
     */
    public ListItemHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mPhoto.setVisibility(VISIBLE);

        if (mOnItemClickListener != null) {
            itemView.setOnClickListener(view -> mOnItemClickListener.onItemClickListener());
            itemView.setOnLongClickListener(view -> {
                mOnItemClickListener.onItemLongClickListener();
                return true;
            });
        }
    }


    public void setBigText(String bigText) {
        mBigText.setText(bigText);
    }

    public void setSmallText(String smallText) {
        mSmallText.setText(smallText);
    }

    public void setHeader(String header) {
        mHeader.setText(header);
    }

    public void showHeader(boolean isShow) {
        mHeader.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    public void showPhoto(boolean isShow, @Nullable Uri image) {
        mPhoto.setVisibility(isShow ? VISIBLE : GONE);
        mPhotoPlaceholder.setVisibility(isShow ? GONE : VISIBLE);
        if (isShow) mPhoto.setImageURI(image);
    }

    public void showPhoto(boolean isShow, @Nullable @DrawableRes int image) {
        mPhoto.setVisibility(isShow ? VISIBLE : GONE);
        mPhotoPlaceholder.setVisibility(isShow ? GONE : VISIBLE);
        if (isShow) mPhoto.setImageResource(image);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener();

        void onItemLongClickListener();
    }
}
