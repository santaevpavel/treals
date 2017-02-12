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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.List;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.activity.SingleNewsActivity;
import ru.nsu.fit.nsuschedule.adapter.EventAdapter;
import ru.nsu.fit.nsuschedule.databinding.FragmentEventsBinding;
import ru.nsu.fit.nsuschedule.model.News;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

/**
 * Created by Pavel on 18.01.2017.
 */

public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, EventAdapter.IEventAdapterParent {

    private IEventsFragmentParent parent;
    private FragmentEventsBinding binding;
    private EventAdapter adapter;

    public static EventsFragment getInstance() {
        return new EventsFragment();
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        v.startAnimation(anim_in);
    }

    @Override
    public void onRefresh() {
        parent.onRefreshEvents();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (IEventsFragmentParent) context;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events, container, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new EventAdapter();
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

    public void setEvents(List<News> news) {
        binding.swipeContainer.setRefreshing(false);
        if (null == news) {
            return;
        }
        adapter.setNews(news);
        adapter.notifyDataSetChanged();
        binding.progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickNews(News news) {
        SingleNewsActivity.start(getActivity(), news.getLink(), "Событие", R.style.AppThemeAcadem);
    }

    @Override
    public void onLoadedImg(int pos, long id, Bitmap bmp, EventAdapter.EventViewHolder holder) {
        if (!isAdded()) {
            return;
        }
        EventAdapter.EventViewHolder holderFromChild = (EventAdapter.EventViewHolder)
                binding.listNews.findViewHolderForItemId(pos);
        if (holder.getItemId() == id) {
            holderFromChild = holder;
        }
        if (holderFromChild == null) {
            return;
        }

        holderFromChild.image.setImageBitmap(bmp);
        holderFromChild.image.setVisibility(View.VISIBLE);
        imageViewAnimatedChange(getActivity(), holderFromChild.image, bmp);
    }

    public interface IEventsFragmentParent {

        void onAttach(EventsFragment fragment);

        void onDetach(EventsFragment fragment);

        void onRefreshEvents();
    }
}
