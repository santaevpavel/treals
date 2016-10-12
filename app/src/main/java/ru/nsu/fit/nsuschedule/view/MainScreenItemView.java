package ru.nsu.fit.nsuschedule.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.R;

/**
 * Created by Pavel on 03.10.2016.
 */
public class MainScreenItemView extends FrameLayout{

    private View backgroundShadow;
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
        backgroundShadow = root.findViewById(R.id.background_shadow);
        background = (ImageView) root.findViewById(R.id.background);
        text = (TextView) root.findViewById(R.id.text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        background.animate();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                backgroundShadow.animate().alpha(0.5f).setDuration(200);
                break;
            case MotionEvent.ACTION_UP:
                backgroundShadow.animate().alpha(1).setDuration(200);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public void setBackground(int id){
        this.background.setImageResource(id);
    }

    public void animateText(final Animator.AnimatorListener listener){
        text.animate().alpha(0f).scaleX(5f).scaleY(5f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.onAnimationEnd(animation);
                text.setAlpha(1);
                text.setScaleX(1);
                text.setScaleY(1);
            }
        }).start();
    }

}
