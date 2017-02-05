package ru.nsu.fit.nsuschedule.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import ru.nsu.fit.nsuschedule.util.PreferenceHelper;

public class AcademActivity extends AppCompatActivity implements NewsFragment.INewsFragmentParent, PlacesFragment.IPlacesFragmentParent {

    public static final String KEY_ACADEM = "KEY_ACADEM";
    private ActivityAcademBinding binding;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private NewsFragment fragmentEvents;
    private PlacesFragment fragmentLocations;

    private Map<String, List<News>> sections;
    private Map<String, List<Place>> placeSections;

    private List<News> news;
    private List<Place> places;

    public static Map<String, List<News>> getNewsSections(List<News> news) {
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

    public static Map<String, List<Place>> getPlaceSections(List<Place> places) {
        Map<String, List<Place>> res = new HashMap<>();
        for (Place n : places) {
            String section = n.getType();
            if (res.containsKey(section)) {
                res.get(section).add(n);
            } else {
                List<Place> placeList = new ArrayList<>();
                placeList.add(n);
                res.put(section, placeList);
            }
        }
        return res;
    }

    public static <T> void showCategoryDialog(Context context, final List<T> list, final Map<String, List<T>> sections,
                                              final IOnCategoryChooseListener<T> listener) {
        if (null == list) return;

        Set<String> keySet = sections.keySet();

        final String[] keys = keySet.toArray(new String[keySet.size()]);
        final boolean[] checked = new boolean[keys.length];
        for (int i = 0; i < checked.length; i++) {
            checked[i] = true;
        }
        new AlertDialog.Builder(context).setMultiChoiceItems(keys, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checked[which] = isChecked;
            }
        }).setPositiveButton("Выбрать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<T> filtered = new ArrayList<>();
                for (int i = 0; i < checked.length; i++) {
                    if (checked[i]) {
                        filtered.addAll(sections.get(keys[i]));
                    }
                }
                //showPlaces(filtered);
                listener.onChoose(checked, filtered);
            }
        }).setTitle("Выберите категории").show();
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
        sections = getNewsSections(news);
        showNews(news);
    }

    private void onPlacesLoaded(List<Place> places) {
        sortPlaces();
        placeSections = getPlaceSections(places);
        showPlaces(places);
    }

    private void sortPlaces() {
        final Pair<Double, Double> location = PreferenceHelper.getLocation();
        if (location != null) {
            final Location me = new Location("Me");
            me.setLatitude(location.first);
            me.setLongitude(location.second);

            for (Place place : places) {
                Location loc = new Location("loc");
                loc.setLatitude(place.getLat());
                loc.setLongitude(place.getLng());
                place.setDist(loc.distanceTo(me));
            }

            Collections.sort(places, new Comparator<Place>() {
                @Override
                public int compare(Place o1, Place o2) {
                    return (int) Math.signum(o1.getDist() - o2.getDist());
                }
            });
        }
    }

    private void onNewsLoadFailed(){
        if (fragmentEvents != null) {
            fragmentEvents.setNews(null);
        }
    }

    private void onPlacesLoadFailed() {
        if (fragmentLocations != null) {
            fragmentLocations.setPlaces(null);
        }
    }

    private void onClickFilter() {
        showCategoryDialog(this, places, placeSections, new IOnCategoryChooseListener<Place>() {
            @Override
            public void onChoose(boolean[] checked, List<Place> checkedItems) {

            }
        });
    }

    private void showNews(List<News> newsList) {
        if (fragmentEvents != null) {
            fragmentEvents.setNews(newsList);
        }
    }

    private void showPlaces(List<Place> newsList) {
        if (fragmentLocations != null) {
            fragmentLocations.setPlaces(newsList);
        }
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

    public interface IOnCategoryChooseListener<T> {
        void onChoose(boolean[] checked, List<T> checkedItems);
    }
}
