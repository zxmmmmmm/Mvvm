package com.bmapleaf.mvvm.help;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;

/**
 * Created by ZhangMing on 2017/7/10.
 */

public class Utils {
    private static final String TAG = "Utils";

    public static <V extends View> V getView(Activity activity, @IdRes int id) {
        return cast(activity.findViewById(id));
    }

    public static <V extends View> V getView(View view, @IdRes int id) {
        return cast(view.findViewById(id));
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        T t = null;
        try {
            t = (T) obj;
        } catch (ClassCastException e) {
            Log.e(TAG, "cast: " + e.getLocalizedMessage());
        }
        return t;
    }
}
