package ru.nsu.fit.nsuschedule.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Pavel on 11.10.2016.
 */
public class CustomFontLoader {

    public static final int CENTURY_GOTHIC_BOLD = 0;
    public static final int CENTURY_GOTHIC_NORMAL = 1;

    public static final int ROBOTO_REGULAR = 2;
    public static final int ROBOTO_BOLD = 3;
    public static final int ROBOTO_MEDIUM = 4;


    private static final int NUM_OF_CUSTOM_FONTS = 5;

    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[5];

    private static String[] fontPath = {
            "fonts/century_gothic_bold.ttf",
            "fonts/century_gothic_normal.ttf",
            "fonts/roboto_regular.ttf",
            "fonts/roboto_bold.ttf",
            "fonts/roboto_medium.ttf",
    };


    /**
     * Returns a loaded custom font based on it's identifier.
     *
     * @param context - the current context
     * @param fontIdentifier = the identifier of the requested font
     *
     * @return Typeface object of the requested font.
     */
    public static Typeface getTypeface(Context context, int fontIdentifier) {
        if (!fontsLoaded) {
            loadFonts(context);
        }
        return fonts[fontIdentifier];
    }


    private static void loadFonts(Context context) {
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        fontsLoaded = true;

    }
}