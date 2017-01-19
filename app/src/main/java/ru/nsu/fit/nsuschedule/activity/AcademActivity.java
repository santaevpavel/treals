package ru.nsu.fit.nsuschedule.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.response.AllNewsResponse;
import ru.nsu.fit.nsuschedule.databinding.ActivityAcademBinding;
import ru.nsu.fit.nsuschedule.fragment.NewsFragment;
import ru.nsu.fit.nsuschedule.model.News;

public class AcademActivity extends AppCompatActivity implements NewsFragment.INewsFragmentParent{

    public static final String KEY_ACADEM = "KEY_ACADEM";
    private ActivityAcademBinding binding;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private NewsFragment fragmentEvents;
    private NewsFragment fragmentLocations;
    private Map<String, List<News>> sections;
    private List<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academ);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_academ);

        binding.toolbar.setTitle("Академ");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadNews();

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return NewsFragment.getInstance(position);
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return 0 == position ? "События" : "Места";
            }

        };
        binding.pager.setAdapter(fragmentPagerAdapter);
        binding.tablayout.setupWithViewPager(binding.pager, true);
    }

    private void loadNews(){
        ApiServiceHelper.getAllAcademNews(this, new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                AllNewsResponse response = (AllNewsResponse)
                        resultData.getSerializable(ApiService.KEY_RESPONSE);
                if (response == null){
                    Snackbar.make(binding.toolbar, "Ошибка", Snackbar.LENGTH_LONG).show();
                    onNewsLoadFailed();
                    return;
                }
                if (response.hasError()){
                    Snackbar.make(binding.toolbar, response.getErrorMsg(), Snackbar.LENGTH_LONG).show();
                    onNewsLoadFailed();
                } else {
                    news = response.news;
                    onNewsLoaded(news);
                }
            }
        });
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

    @Override
    public List<News> getNews(int code) {
        return null;
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

    private void onNewsLoaded(List<News> news){
        sections =  getSections(news);
        showNews(news);
    }

    private void onNewsLoadFailed(){
        fragmentEvents.setNews(null);
        fragmentLocations.setNews(null);
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

    private void showNews(List<News> newsList) {
        fragmentEvents.setNews(newsList);
        fragmentLocations.setNews(newsList);
    }

    @Override
    public void onAttach(NewsFragment fragment, int code) {
        switch (code){
            case 0:
                fragmentEvents = fragment;
                break;
            case 1:
                fragmentLocations = fragment;
                break;
        }
    }

    @Override
    public void onDetach(NewsFragment fragment, int code) {
        fragmentEvents = null;
        fragmentLocations = null;
    }

    @Override
    public void onRefresh(int code) {
        loadNews();
    }
}
