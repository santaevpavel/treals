package ru.nsu.fit.nsuschedule.activity;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.adapter.NewsAdapter;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.response.NewsResponse;
import ru.nsu.fit.nsuschedule.api.response.WeatherResponse;
import ru.nsu.fit.nsuschedule.model.News;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView newRecyclerView;

    private NewsAdapter adapter;
    private Toolbar toolbar;
    private View viewProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        RequestQueue queue = ImageLoaderSingleton.getInstance(
                getApplicationContext()).
                getRequestQueue();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewProgress = findViewById(R.id.progress);
        toolbar.setTitle("Новости НГУ");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new NewsAdapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        newRecyclerView = (RecyclerView) findViewById(R.id.listNews);
        newRecyclerView.setLayoutManager(mLayoutManager);
        newRecyclerView.setAdapter(adapter);

        ApiServiceHelper.getNews(this, new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                NewsResponse response = (NewsResponse)
                        resultData.getSerializable(ApiService.KEY_RESPONSE);
                if (response == null){
                    Snackbar.make(newRecyclerView, "Ошибка", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (response.hasError()){
                    Snackbar.make(newRecyclerView, response.getErrorMsg(), Snackbar.LENGTH_LONG).show();
                } else {
                    adapter.setNews(response.news);
                    adapter.notifyDataSetChanged();
                    viewProgress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
