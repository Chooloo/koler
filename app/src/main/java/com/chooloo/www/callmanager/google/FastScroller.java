/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chooloo.www.callmanager.google;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.AbsFastScrollerAdapter;

public class FastScroller extends RelativeLayout {

    private final int touchTargetWidth;

    private AbsFastScrollerAdapter adapter;
    private LinearLayoutManager layoutManager;

    private boolean dragStarted;

    // Views
    private TextView container;
    private View scrollBar;

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public FastScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchTargetWidth =
                context.getResources().getDimensionPixelSize(R.dimen.fast_scroller_touch_target_width);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        container = findViewById(R.id.fast_scroller_container);
        scrollBar = findViewById(R.id.fast_scroller_scroll_bar);
    }

    public void setup(AbsFastScrollerAdapter adapter, LinearLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
        setVisibility(VISIBLE);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        // Don't override if touch event isn't within desired touch target and dragging hasn't started.
        if (!dragStarted && getWidth() - touchTargetWidth - event.getX() > 0) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dragStarted = true;
                container.setVisibility(VISIBLE);
                scrollBar.setSelected(true);
                // fall through
            case MotionEvent.ACTION_MOVE:
                setContainerAndScrollBarPosition(event.getY());
                setRecyclerViewPosition(event.getY());
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                dragStarted = false;
                container.setVisibility(INVISIBLE);
                scrollBar.setSelected(false);
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public boolean isDragStarted() {
        return dragStarted;
    }

    /**
     * Sets the position of the recycler view by a given number (y)
     *
     * @param y
     */
    private void setRecyclerViewPosition(float y) {
        final int itemCount = adapter.getItemCount();
        float scrolledPosition = getScrolledPercentage(y) * (float) itemCount;
        int targetPos = getValueInRange(0, itemCount - 1, (int) scrolledPosition);
        layoutManager.scrollToPositionWithOffset(targetPos, 0);

        String header = adapter.getHeaderString(targetPos);
        if (header == null) {
            container.setVisibility(INVISIBLE);
        } else {
            container.setVisibility(VISIBLE);
            container.setText(header);
        }
    }

    /**
     * Returns a float in range [0, 1] which represents the position of the scroller.
     *
     * @param y
     * @return float
     */
    private float getScrolledPercentage(float y) {
        if (scrollBar.getY() == 0) {
            return 0f;
        } else if (scrollBar.getY() + scrollBar.getHeight() >= getHeight()) {
            return 1f;
        } else {
            return y / (float) getHeight();
        }
    }

    /**
     * Returns the value in range of scrolling
     *
     * @param min
     * @param max
     * @param value
     * @return int
     */
    private int getValueInRange(int min, int max, int value) {
        int minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    /**
     * Updated the scroll bar position
     *
     * @param recyclerView
     */
    public void updateContainerAndScrollBarPosition(RecyclerView recyclerView) {
        if (!scrollBar.isSelected()) {
            int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
            int verticalScrollRange = recyclerView.computeVerticalScrollRange();
            float proportion = (float) verticalScrollOffset / ((float) verticalScrollRange - getHeight());
            setContainerAndScrollBarPosition(getHeight() * proportion);
        }
    }

    /**
     * Sets the scroll bar position by a given number
     *
     * @param y
     */
    private void setContainerAndScrollBarPosition(float y) {
        int scrollBarHeight = scrollBar.getHeight();
        int containerHeight = container.getHeight();
        scrollBar.setY(
                getValueInRange(0, getHeight() - scrollBarHeight, (int) (y - scrollBarHeight / 2)));
        container.setY(
                getValueInRange(
                        0, getHeight() - containerHeight - scrollBarHeight / 2, (int) (y - containerHeight)));
    }
}
