package ru.nsu.fit.nsuschedule.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.List;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.activity.AcademActivity;
import ru.nsu.fit.nsuschedule.activity.PlaceActivity;
import ru.nsu.fit.nsuschedule.adapter.PlacesAdapter;
import ru.nsu.fit.nsuschedule.databinding.FragmentPlacesBinding;
import ru.nsu.fit.nsuschedule.model.Place;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

/**
 * Created by Pavel on 18.01.2017.
 */

public class PlacesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PlacesAdapter.IPlacesAdapterParent {

    private IPlacesFragmentParent parent;
    private FragmentPlacesBinding binding;
    private PlacesAdapter adapter;

    public static PlacesFragment getInstance() {
        return new PlacesFragment();
    }

    private static boolean isSmall(Bitmap bmp) {
        int minWidth = 256;
        double minRate = 3. / 4;
        return bmp.getWidth() < minWidth || minRate < ((double) bmp.getHeight() / bmp.getWidth());
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v) {
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        v.startAnimation(anim_in);
    }

    @Override
    public void onRefresh() {
        parent.onRefreshPlaces();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (IPlacesFragmentParent) context;
        parent.onAttach(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderSingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_places, container, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new PlacesAdapter();
        adapter.setListener(this);
        binding.listNews.setAdapter(adapter);
        binding.listNews.setLayoutManager(mLayoutManager);

        // Configure the refreshing colors
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        binding.swipeContainer.setOnRefreshListener(this);

        return binding.getRoot();
    }

    public void setPlaces(List<Place> places) {
        binding.swipeContainer.setRefreshing(false);
        if (null == places) {
            return;
        }
        adapter.setPlaces(places);
        adapter.notifyDataSetChanged();
        binding.progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_places, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.filter:
                AcademActivity activity = (AcademActivity) getActivity();
                if (activity != null) {
                    activity.onClickFilter();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickPlace(Place place) {
        PlaceActivity.start(getActivity(), place);
    }

    public void onLoadedImg(int pos, long id, Bitmap bmp, PlacesAdapter.PlacesViewHolder placesViewHolder) {
        if (!isAdded()) {
            return;
        }
        PlacesAdapter.PlacesViewHolder holderFromChild = (PlacesAdapter.PlacesViewHolder)
                binding.listNews.findViewHolderForItemId(pos);
        if (placesViewHolder.getItemId() == id) {
            holderFromChild = placesViewHolder;
        }
        if (holderFromChild == null) {
            return;
        }
        holderFromChild.binding.image.setImageBitmap(bmp);
        holderFromChild.binding.image.setVisibility(View.VISIBLE);
        imageViewAnimatedChange(getActivity(), holderFromChild.binding.image);
    }

    public interface IPlacesFragmentParent {

        void onAttach(PlacesFragment fragment);

        void onDetach(PlacesFragment fragment);

        void onRefreshPlaces();
    }
}
