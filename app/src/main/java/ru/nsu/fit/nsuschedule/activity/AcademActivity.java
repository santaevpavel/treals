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
import ru.nsu.fit.nsuschedule.api.response.AllPlacesResponse;
import ru.nsu.fit.nsuschedule.databinding.ActivityAcademBinding;
import ru.nsu.fit.nsuschedule.fragment.NewsFragment;
import ru.nsu.fit.nsuschedule.fragment.PlacesFragment;
import ru.nsu.fit.nsuschedule.model.News;
import ru.nsu.fit.nsuschedule.model.Place;

public class AcademActivity extends AppCompatActivity implements NewsFragment.INewsFragmentParent, PlacesFragment.IPlacesFragmentParent {

    public static final String KEY_ACADEM = "KEY_ACADEM";
    private ActivityAcademBinding binding;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private NewsFragment fragmentEvents;
    private PlacesFragment fragmentLocations;
    private Map<String, List<News>> sections;
    private List<News> news;

    private List<Place> places;

    public static Map<String, List<News>> getSections(List<News> news) {
        Map<String, List<News>> res = new HashMap<>();
        for (News n : news) {
            String section = n.getSection();
            if (res.containsKey(section)) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_academ);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_academ);

        binding.toolbar.setTitle("Академ");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadNews();
        //loadPlaces();

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return 0 == position ? NewsFragment.getInstance(position) : PlacesFragment.getInstance(position);
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
                    loadPlaces();
                }
            }
        });
    }

    private void loadPlaces() {
        ApiServiceHelper.getAllPlaces(this, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                AllPlacesResponse response = (AllPlacesResponse)
                        resultData.getSerializable(ApiService.KEY_RESPONSE);
                if (response == null) {
                    Snackbar.make(binding.toolbar, "Ошибка", Snackbar.LENGTH_LONG).show();
                    onPlacesLoadFailed();
                    return;
                }
                if (response.hasError()) {
                    Snackbar.make(binding.toolbar, response.getErrorMsg(), Snackbar.LENGTH_LONG).show();
                    onPlacesLoadFailed();
                } else {
                    places = response.places;
                    onPlacesLoaded(places);
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
        return news;
    }

    private void onNewsLoaded(List<News> news){
        sections =  getSections(news);
        showNews(news);
    }

    private void onPlacesLoaded(List<Place> places) {
        showPlaces(places);
    }

    private void onNewsLoadFailed(){
        fragmentEvents.setNews(null);
    }

    private void onPlacesLoadFailed() {
        fragmentLocations.setPlaces(null);
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
    }

    private void showPlaces(List<Place> newsList) {
        fragmentLocations.setPlaces(newsList);
    }

    @Override
    public void onAttach(NewsFragment fragment, int code) {
        fragmentEvents = fragment;
    }

    @Override
    public void onDetach(NewsFragment fragment, int code) {
        fragmentEvents = null;
        fragmentLocations = null;
    }

    @Override
    public List<Place> getPlaces(int code) {
        return places;
    }

    @Override
    public void onAttach(PlacesFragment fragment, int code) {
        fragmentLocations = fragment;
    }

    @Override
    public void onDetach(PlacesFragment fragment, int code) {
        fragmentLocations = null;
    }

    @Override
    public void onRefresh(int code) {
        switch (code) {
            case 0:
                loadNews();
                break;
            case 1:
                loadPlaces();
                break;
        }
    }
}
