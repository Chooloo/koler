package com.chooloo.www.callmanager.util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.animation.AnimationUtils.loadAnimation;
import static android.view.animation.AnimationUtils.loadLayoutAnimation;

public class AnimationUtils {
    public static final int ANIMATION_DURATION = 500;

    public static void showView(View view, boolean isShow) {
        view.setVisibility(isShow ? VISIBLE : GONE);
        view.startAnimation(loadAnimation(view.getContext(), isShow ? R.anim.animation_fall_down_show : R.anim.animation_fall_down_hide));
    }

    public static void setFadeAnimation(View view) {
        view.startAnimation(loadAnimation(view.getContext(), R.anim.animation_fade_in));
    }

    public static void setFadeUpAnimation(View view) {
        view.startAnimation(loadAnimation(view.getContext(), R.anim.animation_fall_down_show));
    }

    public static void runLayoutAnimation(final RecyclerView recyclerView) {
        recyclerView.setLayoutAnimation(loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation_fall_down));
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public static void animateViews(View[] views, int delay, boolean isShow) {
        for (View view : views) {
            view.setVisibility(INVISIBLE);
        }
        for (int i = 0; i < views.length; i++) {
            final View view = views[i];
            view.postDelayed(() -> {
                view.setVisibility(VISIBLE);
                showView(view, isShow);
            }, i * delay);
        }
    }
}
