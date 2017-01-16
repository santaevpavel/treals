package ru.nsu.fit.nsuschedule.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;

/**
 * Created by Pavel on 03.10.2016.
 */
public class Helper {

    public static void hideKeyboard(Window window){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static Drawable tintDrawable(Resources resources, int drawableId, int colorSelector){
        ColorStateList colours = ResourcesCompat.getColorStateList(resources, colorSelector, null);
        Drawable d = DrawableCompat.wrap(ResourcesCompat.getDrawable(resources, drawableId, null));
        DrawableCompat.setTintList(d, colours);
        return d;
    }

    public static void addStatusBarPaddingToView(View v, Context context){
        int statusBarHeight = getStatusBarHeight(context.getResources());
        v.setPadding(v.getPaddingLeft(), v.getPaddingTop() + statusBarHeight,
                v.getPaddingRight(), v.getPaddingBottom());
    }

    public static int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Calendar getMondayOfWeek(Calendar today){
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int mondayOffset = ((dayOfWeek - 2) + 7) % 7;

        Calendar monday = (Calendar) today.clone();
        monday.add(Calendar.DATE, -1 * mondayOffset);
        return monday;
    }

    public static String removeQuotes(String str){
        return str.replace("&quot;", "\"");
    }
}
