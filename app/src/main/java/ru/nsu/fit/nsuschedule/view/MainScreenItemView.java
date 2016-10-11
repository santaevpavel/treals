package ru.nsu.fit.nsuschedule.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.R;

/**
 * Created by Pavel on 03.10.2016.
 */
public class MainScreenItemView extends FrameLayout{

    private ImageView background;
    private TextView text;

    public MainScreenItemView(Context context) {
        super(context);
        init();
    }

    public MainScreenItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainScreenItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MainScreenItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        View root = View.inflate(getContext(), R.layout.main_screen_item, this);
        background = (ImageView) root.findViewById(R.id.background);
        text = (TextView) root.findViewById(R.id.text);
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public void setBackground(int id){
        this.background.setImageResource(id);
    }
}
