package ru.nsu.fit.nsuschedule.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.adapter.PlacesAdapter;
import ru.nsu.fit.nsuschedule.databinding.ActivityPlaceBinding;
import ru.nsu.fit.nsuschedule.fragment.PlaceFragment;
import ru.nsu.fit.nsuschedule.model.Place;
import ru.nsu.fit.nsuschedule.util.Helper;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

public class PlaceActivity extends AppCompatActivity {

    public static final String KEY_PLACE = "KEY_PLACE";

    private Place place;
    private ActivityPlaceBinding binding;

    public static void start(Activity activity, Place place) {
        Intent intent = new Intent(activity, PlaceActivity.class);
        intent.putExtra(KEY_PLACE, place);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        place = (Place) getIntent().getSerializableExtra(KEY_PLACE);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbar.setTitle(place.getTitle());

        toolbar.setTitle(" ");
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        lp.setMargins(0, getStatusBarHeight(), 0, 0);

        ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext()).getImageLoader()
                .get(place.getImg(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap bitmap = response.getBitmap();
                        if (null != bitmap) {
                            ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);
                            binding.appBarLayout.setExpanded(true, true);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.appBarLayout.setExpanded(false, true);
                    }
                });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceFragment.getInstance(place)).commit();

        binding.itemTitle.setText(place.getTitle());
        binding.itemType.setText(place.getType());
        binding.distance.setText(PlacesAdapter.getDistanceString((int) place.getDist()));

        //binding.appBarLayout.setExpanded(false, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_found_error:
                Helper.sendEmail(this, "Сообщение об ошибке", String.format("Место: %s\n", place.getTitle()), "Сообщить об ошибке");
                break;
            case R.id.action_offer_place:
                Helper.sendEmail(this, "Предложить место", "", "Предложить место");
                break;
        }
        return true;
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
