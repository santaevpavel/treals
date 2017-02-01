package ru.nsu.fit.nsuschedule.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

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

public class PlaceFragment extends Fragment implements View.OnClickListener {

    public static final String KEY_PLACE = "KEY_PLACE";
    public static final int MAX_SIZE_DESCRIPTION = 200;

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

        binding.address.setText(place.getPlace());

        binding.time.setText(place.getTime());
        binding.phone.setText(place.getPhone());

        binding.price.setText(place.getPrice() + " руб.");

        binding.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMapInBrowser();
            }
        });

        binding.site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openUrl(place.get
            }
        });

        binding.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoneDialog();
            }
        });

        addPaddingToItem(binding.phoneItem, place.getPhone());
        addPaddingToItem(binding.timeItem, place.getTime());

        boolean isNeedShowExpand = place.getDescription().length() > MAX_SIZE_DESCRIPTION;
        String shortDescr = isNeedShowExpand
                ? place.getDescription().substring(0, MAX_SIZE_DESCRIPTION) + "..."
                : place.getDescription();

        binding.descr.setText(shortDescr);
        binding.descr.setVisibility(place.getDescription().isEmpty() ? View.GONE : View.VISIBLE);

        binding.descrExpand.setVisibility(isNeedShowExpand ? View.VISIBLE : View.GONE);

        binding.descrExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.descr.setText(place.getDescription());
                binding.descrExpand.setVisibility(View.GONE);
            }
        });

        String mapUrl = String.format(Locale.ENGLISH,
                "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=17&size=640x300&maptype=roadmap" +
                        "&scale=2&markers=%f,%f&key=AIzaSyDN6VPwntOEAb93xQUQPrm0PvUyNl2xbM4",
                place.getLat(), place.getLng(), place.getLat(), place.getLng());

        ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext()).getImageLoader()
                .get(mapUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap bitmap = response.getBitmap();
                        if (null != bitmap) {
                            binding.map.setImageBitmap(bitmap);
                            binding.map.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openGoogleMapInBrowser();
                                }
                            });
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        return binding.getRoot();
    }

    private void openGoogleMapInBrowser() {
        String mapUrl = String.format(Locale.ENGLISH, "https://www.google.ru/maps?&z=18&q=%f+%f&ll%f+%f",
                place.getLat(), place.getLng(), place.getLat(), place.getLng());
        openUrl(mapUrl);
    }

    private void openUrl(String url) {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void openPhoneDialog() {
        final String[] phones = place.getPhone().split("\\n");

        if (phones.length == 1) {
            openPhone(phones[0]);
            return;
        }

        new AlertDialog.Builder(getActivity())
                .setTitle("Позвонить")
                .setSingleChoiceItems(phones, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openPhone(phones[which]);
                    }
                }).show();
    }

    private void openPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void addPaddingToItem(View view, String text) {
        int phoneCount = text.split("\\n").length;
        int additionalPadding = (int) ((phoneCount - 1) * getResources().getDimension(R.dimen.place_item_height_add));
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = layoutParams.height + additionalPadding;
        view.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        openGoogleMapInBrowser();
    }

}

