package ru.nsu.fit.nsuschedule.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.util.CustomFontLoader;

/**
 * Created by Pavel on 10.10.2016.
 */
public class TextViewCenturyGothic extends TextView{

    public TextViewCenturyGothic(Context context) {
        super(context);
        init();
    }

    public TextViewCenturyGothic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCenturyGothic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TextViewCenturyGothic(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setTypeface(CustomFontLoader.getTypeface(getContext(), CustomFontLoader.CENTURY_GOTHIC_NORMAL));
    }
}
