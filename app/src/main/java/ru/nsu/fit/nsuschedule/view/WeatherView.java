package ru.nsu.fit.nsuschedule.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.R;

/**
 * Created by Pavel on 06.10.2016.
 */
public class WeatherView extends FrameLayout {

    private TextView temp;
    private TextView status;
    private ProgressBar progressBar;
    private ImageView icon;
    private boolean isWeatherSet = false;

    public WeatherView(Context context) {
        super(context);
        init();
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public boolean isWeatherSet() {
        return isWeatherSet;
    }

    public void setTemp(String text){
        isWeatherSet = true;
        temp.setText(text);
        status.setVisibility(GONE);

        progressBar.setVisibility(GONE);

        temp.setAlpha(0);
        temp.setVisibility(View.VISIBLE);
        temp.animate().alpha(1.0f).setDuration(150);
        icon.setVisibility(View.VISIBLE);
    }

    public void setStatus(String text){
        status.setText(text);
        status.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        temp.setVisibility(GONE);
        icon.setVisibility(GONE);
    }

    public void showProgress(){
        progressBar.setVisibility(VISIBLE);
        status.setVisibility(GONE);
        temp.setVisibility(GONE);
        icon.setVisibility(GONE);
    }

    private void init(){
        View root = View.inflate(getContext(), R.layout.weather_layout, this);
        temp = (TextView) root.findViewById(R.id.text_weather);
        status = (TextView) root.findViewById(R.id.text_status);
        progressBar = (ProgressBar) root.findViewById(R.id.progress);
        icon = (ImageView) root.findViewById(R.id.icon);
        icon.setVisibility(GONE);
        setTemp("");
        isWeatherSet = false;
    }
}
