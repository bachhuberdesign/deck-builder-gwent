package com.bachhuberdesign.gwentcardviewer.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler;

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
public class SlideInChangeHandler extends AnimatorChangeHandler {

    public SlideInChangeHandler() {
    }

    public SlideInChangeHandler(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    public SlideInChangeHandler(long animationDuration, boolean isEntrance) {
        this.animationDuration = animationDuration;
        this.isEntrance = isEntrance;
    }

    private static final long DEFAULT_ANIMATION_DURATION = 300;

    private long animationDuration = 0;
    private boolean isEntrance = true;

    @NonNull
    @Override
    protected Animator getAnimator(@NonNull ViewGroup container, @Nullable View from, @Nullable View to, boolean isPush, boolean toAddedToContainer) {
        AnimatorSet animatorSet = new AnimatorSet();

        if (animationDuration == 0) {
            animationDuration = DEFAULT_ANIMATION_DURATION;
        }

        if (to != null) {
            to.setAlpha(0);

            ObjectAnimator translator = null;
            if (isEntrance) {
                translator = ObjectAnimator.ofFloat(to, View.TRANSLATION_Y, 2000, 0).setDuration(animationDuration);
            } else {
                translator = ObjectAnimator.ofFloat(from, View.TRANSLATION_Y, 0, 2000).setDuration(animationDuration);
            }

            translator.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.play(translator);

            Animator alpha = ObjectAnimator.ofFloat(to, View.ALPHA, 1).setDuration(animationDuration / 2);
            alpha.setStartDelay(animationDuration / 3);
            animatorSet.play(alpha);
        }

        return animatorSet;
    }

    @Override
    protected void resetFromView(@NonNull View from) {
        from.setAlpha(1.0f);
    }

}
