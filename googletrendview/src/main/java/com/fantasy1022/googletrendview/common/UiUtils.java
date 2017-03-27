package com.fantasy1022.googletrendview.common;

import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;

import android.view.ViewGroup;

/**
 * Created by fantasy1022 on 2017/2/10.
 */

public class UiUtils {

    public static void animateForViewGroupTransition(ViewGroup viewGroup) {
        ChangeBounds changeBounds = new ChangeBounds();
        Fade fadeOut = new Fade(Fade.OUT);
        Fade fadeIn = new Fade(Fade.IN);
        TransitionSet transition = new TransitionSet();
        transition.setOrdering(TransitionSet.ORDERING_TOGETHER);
        transition.addTransition(fadeOut)
                .addTransition(changeBounds)
                .addTransition(fadeIn);

        TransitionManager.beginDelayedTransition(viewGroup);
    }
}
