package ru.nsu.fit.nsuschedule.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.fragment.PlaceFragment;
import ru.nsu.fit.nsuschedule.model.Place;
import ru.nsu.fit.nsuschedule.util.CustomFontLoader;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

public class PlaceActivity extends AppCompatActivity {

    public static final String KEY_PLACE = "KEY_PLACE";

    private Place place;

    public static void start(Activity activity, Place place) {
        Intent intent = new Intent(activity, PlaceActivity.class);
        intent.putExtra(KEY_PLACE, place);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        place = (Place) getIntent().getSerializableExtra(KEY_PLACE);

        setContentView(R.layout.activity_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbar.setTitle(place.getTitle());
        collapsingToolbar.setExpandedTitleTypeface(CustomFontLoader.getTypeface(this, CustomFontLoader.CENTURY_GOTHIC_BOLD));
        //collapsingToolbar.setStatusBarScrimColor(getResources().getColor(android.R.color.holo_red_dark));

        ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext()).getImageLoader()
                .get(place.getImg(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap bitmap = response.getBitmap();
                        if (null != bitmap) {
                            ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceFragment.getInstance(place)).commit();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
