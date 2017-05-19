package com.julius.popularmovies.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

/**
 * Created by ameh on 16/05/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ImageTransformTransition extends TransitionSet {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageTransformTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds().setDuration(300))
                .addTransition(new ChangeTransform());
    }
}