package ru.nsu.fit.nsuschedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.util.CustomFontLoader;

/**
 * Created by Pavel on 10.10.2016.
 */
public class TextViewRobotoMedium extends TextView {

    public TextViewRobotoMedium(Context context) {
        super(context);
        init();
    }

    public TextViewRobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRobotoMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TextViewRobotoMedium(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTypeface(CustomFontLoader.getTypeface(getContext(), CustomFontLoader.ROBOTO_MEDIUM));
    }
}
