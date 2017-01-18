package ru.nsu.fit.nsuschedule.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.adapter.NewsAdapter;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.response.AllNewsResponse;
import ru.nsu.fit.nsuschedule.databinding.ActivityNewsBinding;
import ru.nsu.fit.nsuschedule.fragment.NewsFragment;
import ru.nsu.fit.nsuschedule.model.News;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.IOnNewsClickListener, NewsFragment.INewsFragmentParent {

    public static final String KEY_ACADEM = "news";
    private List<News> news;
    private Map<String, List<News>> sections;
    private ActivityNewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_news);

        binding.toolbar.setTitle("Новости НГУ");
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, NewsFragment.getInstance(0), KEY_ACADEM)
                .commit();

        ApiServiceHelper.getAllNews(this, new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                AllNewsResponse response = (AllNewsResponse)
                        resultData.getSerializable(ApiService.KEY_RESPONSE);
                if (response == null){
                    Snackbar.make(binding.toolbar, "Ошибка", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (response.hasError()){
                    Snackbar.make(binding.toolbar, response.getErrorMsg(), Snackbar.LENGTH_LONG).show();
                } else {
                    news = response.news;

                    onNewsLoaded(news);
                }
            }
        });
    }

    public static Map<String, List<News>> getSections(List<News> news){
        Map<String, List<News>> res = new HashMap<>();
        for (News n : news) {
            String section = n.getSection();
            if (res.containsKey(section)){
                res.get(section).add(n);
            } else {
                List<News> newsList = new ArrayList<>();
                newsList.add(n);
                res.put(section, newsList);
            }
        }
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.filter:
                onClickFilter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onNewsLoaded(List<News> news){
        sections = getSections(news);
        showNews(news);
    }

    private void onClickFilter() {
        if (null == news){
            return;
        }
        final Set<String> keySet = sections.keySet();

        final String[] keys = keySet.toArray(new String[keySet.size()]);
        final boolean[] checked = new boolean[keys.length];
        for (int i = 0; i < checked.length; i++) {
            checked[i] = true;
        }
        new AlertDialog.Builder(this).setMultiChoiceItems(keys, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checked[which] = isChecked;
            }
        }).setPositiveButton("Выбрать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<News> filtered = new ArrayList<News>();
                for (int i = 0; i < checked.length; i++) {
                    if (checked[i]){
                        filtered.addAll(sections.get(keys[i]));
                    }
                }
                showNews(filtered);
            }
        }).setTitle("Выберите категории").show();
    }

    private void showNews(List<News> news){
        NewsFragment newsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag(KEY_ACADEM);
        newsFragment.setNews(news);
    }

    @Override
    public void onClick(News news) {
        SingleNewsActivity.start(this, news.getLink());
    }

    @Override
    public List<News> getNews() {
        return null;
    }

    @Override
    public void onAttach(NewsFragment fragment, int code) {}

    @Override
    public void onDetach(NewsFragment fragment, int code) {}
}