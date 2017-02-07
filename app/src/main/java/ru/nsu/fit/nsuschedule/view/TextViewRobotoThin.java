package ru.nsu.fit.nsuschedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.util.CustomFontLoader;

/**
 * Created by Pavel on 10.10.2016.
 */
public class TextViewRobotoThin extends TextView {

    public TextViewRobotoThin(Context context) {
        super(context);
        init();
    }

    public TextViewRobotoThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRobotoThin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TextViewRobotoThin(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTypeface(CustomFontLoader.getTypeface(getContext(), CustomFontLoader.ROBOTO_THIN));
    }
}
