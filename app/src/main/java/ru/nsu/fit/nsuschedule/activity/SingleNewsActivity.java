package ru.nsu.fit.nsuschedule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.response.NewsResponse;

public class SingleNewsActivity extends AppCompatActivity {

    public static final String KEY_URL = "KEY_URL";

    private TextView text;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);

        url = getIntent().getStringExtra(KEY_URL);
        text = (TextView) findViewById(R.id.text);

        ApiServiceHelper.getNews(this, new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                NewsResponse response = (NewsResponse)
                        resultData.getSerializable(ApiService.KEY_RESPONSE);
                if (response == null){
                    Snackbar.make(text, "Ошибка", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (response.hasError()){
                    Snackbar.make(text, response.getErrorMsg(), Snackbar.LENGTH_LONG).show();
                } else {
                    text.setText(response.news);
                }
            }
        }, url);
    }

    public static void start(Activity activity, String url){
        Intent intent = new Intent(activity, SingleNewsActivity.class);
        intent.putExtra(KEY_URL, url);
        activity.startActivity(intent);
    }
}
