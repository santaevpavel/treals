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
import ru.nsu.fit.nsuschedule.adapter.NewsAdapter;
import ru.nsu.fit.nsuschedule.databinding.FragmentNewsBinding;
import ru.nsu.fit.nsuschedule.model.News;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

/**
 * Created by Pavel on 18.01.2017.
 */

public class NewsFragment extends Fragment implements NewsAdapter.INewsAdapterParent, SwipeRefreshLayout.OnRefreshListener {

    public static final String KEY_CODE = "KEY_CODE";
    private INewsFragmentParent parent;
    private FragmentNewsBinding binding;
    private NewsAdapter adapter;
    private int code;

    public static NewsFragment getInstance(int code){
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    private static boolean isSmall(Bitmap bmp) {
        int minWidth = 256 + 128;
        double minRate = 3. / 4;
        return bmp.getWidth() < minWidth || minRate < ((double) bmp.getHeight() / bmp.getWidth());
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        v.startAnimation(anim_in);
    }

    @Override
    public void onRefresh() {
        parent.onRefresh(code);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        code = getArguments().getInt(KEY_CODE);
        parent = (INewsFragmentParent) context;
        parent.onAttach(this, code);
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new NewsAdapter();
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

    public void setNews(List<News> news){
        binding.swipeContainer.setRefreshing(false);
        if (null == news){
            return;
        }
        adapter.setNews(news);
        adapter.notifyDataSetChanged();
        binding.progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickNews(News news) {
        SingleNewsActivity.start(getActivity(), news.getLink(), "Новости", R.style.AppTheme);
    }

    @Override
    public void onLoadedImg(int pos, long id, Bitmap bmp, NewsAdapter.NewsViewHolder newsHolder) {
        if (!isAdded()){
            return;
        }
        NewsAdapter.NewsViewHolder holderFromChild = (NewsAdapter.NewsViewHolder)
                binding.listNews.findViewHolderForItemId(pos);
        if (newsHolder.getItemId() == id){
            holderFromChild = newsHolder;
        }
        if (holderFromChild == null){
            return;
        }
        boolean small = isSmall(bmp);

        int paddindHoriz = (int) getResources().getDimension(R.dimen.news_item_content_padding_horiz);
        int paddindBottom = (int) getResources().getDimension(R.dimen.news_item_content_padding_bottom);
        int paddindTop = (int) getResources().getDimension(small
                ? R.dimen.news_item_content_padding_top_small
                : R.dimen.news_item_content_padding_top_big);

        holderFromChild.content.setPadding(paddindHoriz, paddindTop, paddindHoriz, paddindBottom);

        if (small) {
            holderFromChild.imageSmall.setImageBitmap(bmp);
            holderFromChild.image.setVisibility(View.GONE);
            holderFromChild.imageSmall.setVisibility(View.VISIBLE);
            imageViewAnimatedChange(getActivity(), holderFromChild.imageSmall, bmp);
        } else {
            holderFromChild.image.setImageBitmap(bmp);
            holderFromChild.image.setVisibility(View.VISIBLE);
            holderFromChild.imageSmall.setVisibility(View.GONE);
            imageViewAnimatedChange(getActivity(), holderFromChild.image, bmp);
        }
    }

    public interface INewsFragmentParent {
        List<News> getNews(int code);

        void onAttach(NewsFragment fragment, int code);

        void onDetach(NewsFragment fragment, int code);

        void onRefresh(int code);
    }
}
