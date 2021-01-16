package com.chooloo.www.callmanager.util;

import android.content.Context;
import android.view.View;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;

import static android.view.animation.AnimationUtils.loadAnimation;

public class AnimationUtils {
    public static final int ANIMATION_DURATION = 500;

    public static void setFadeAnimation(View view, Context context) {
        view.startAnimation(loadAnimation(context, R.anim.animation_fade_in));
    }

    public static void setFadeUpAnimation(View view, Context context) {
        view.startAnimation(loadAnimation(context, R.anim.animation_fall_down));
    }

    public static void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                android.view.animation.AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
