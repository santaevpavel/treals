package ru.nsu.fit.nsuschedule.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.Locale;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.databinding.FragmentPlacesBinding;
import ru.nsu.fit.nsuschedule.model.Place;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

/**
 * Created by Pavel on 18.01.2017.
 */

public class PlaceFragment extends Fragment {

    public static final String KEY_PLACE = "KEY_PLACE";

    private FragmentPlacesBinding binding;
    private Place place;

    public static PlaceFragment getInstance(Place place) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PLACE, place);
        fragment.setArguments(args);
        return fragment;
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        v.startAnimation(anim_in);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        place = (Place) getArguments().getSerializable(KEY_PLACE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderSingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_places, container, false);

        binding.descr.setText(place.getDescription());
        binding.address.setText(place.getPlace());

        //binding.title.setText(place.getTitle());
        binding.time.setText(place.getTime());
        binding.phone.setText(place.getPhone());

        /*ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext()).getImageLoader()
                .get(place.getImg(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap bitmap = response.getBitmap();
                        if (null != bitmap) {
                            //binding.photo.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });*/

        /*String mapUrl = String.format(Locale.ENGLISH,
                "https://static-maps.yandex.ru/1.x/?ll=%f,%f&size=650,450&z=17&l=map",
                place.getLng(), place.getLat());*/
        /*String mapUrl = String.format(Locale.ENGLISH,
                "http://static.maps.2gis.com/1.0?zoom=17&size=1080,600&markers=%f,%f",
                place.getLng(), place.getLat());*/

        String mapUrl = String.format(Locale.ENGLISH,
                "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=17&size=640x300&maptype=roadmap\n" +
                        "&scale=2&markers=color:red|size:mid|%f,%f&key=AIzaSyDN6VPwntOEAb93xQUQPrm0PvUyNl2xbM4",
                place.getLat(), place.getLng(), place.getLat(), place.getLng());

        ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext()).getImageLoader()
                .get(mapUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap bitmap = response.getBitmap();
                        if (null != bitmap) {
                            binding.map.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        return binding.getRoot();
    }
}

