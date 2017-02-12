package ru.nsu.fit.nsuschedule.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import ru.nsu.fit.nsuschedule.R;

public class SingleNewsActivity extends AppCompatActivity {

    public static final String KEY_URL = "KEY_URL";
    public static final String KEY_THEME = "KEY_THEME";
    public static final String KEY_TITLE = "KEY_TITLE";

    private String url;
    private WebView webView;
    private ProgressBar progressBar;

    public static void start(Activity activity, String url, String title, int theme) {
        Intent intent = new Intent(activity, SingleNewsActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_THEME, theme);
        intent.putExtra(KEY_TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getIntent().getIntExtra(KEY_THEME, R.style.AppThemeNews));
        setContentView(R.layout.activity_single_news);


        url = getIntent().getStringExtra(KEY_URL);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(KEY_TITLE));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Snackbar.make(webView, "Не удалось загрузить страницу", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    startActivity(browserIntent);
                }
                return true;
            }
        });

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setMinimumFontSize(40);

        String url = String.format("http://token-shop.ru/api/NEWS/%s", this.url);
        webView.loadUrl(url);
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
