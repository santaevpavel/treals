package ru.nsu.fit.nsuschedule.fragment;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class NewsFragment extends Fragment implements NewsAdapter.IOnNewsClickListener {

    private INewsFragmentParent parent;

    public interface INewsFragmentParent{
        List<News> getNews();
        void onAttach(NewsFragment fragment, int code);
        void onDetach(NewsFragment fragment, int code);
    }

    public static final String KEY_CODE = "KEY_CODE";
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
        return binding.getRoot();
    }

    public void setNews(List<News> news){
        adapter.setNews(news);
        adapter.notifyDataSetChanged();
        binding.progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(News news) {
        SingleNewsActivity.start(getActivity(), news.getLink());
    }
}
