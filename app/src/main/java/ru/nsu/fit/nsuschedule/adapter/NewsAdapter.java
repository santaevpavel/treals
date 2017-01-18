package ru.nsu.fit.nsuschedule.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.model.News;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

/**
 * Created by Pavel on 19.10.2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public interface IOnNewsClickListener{
        void onClick(News news);
    }

    private List<News> news;
    private IOnNewsClickListener listener;

    private class NewsViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView desc;
        public TextView date;
        public TextView type;
        public ImageView image;
        public ImageView imageSmall;

        public NewsViewHolder(View itemView, TextView title,
                              TextView desc, TextView date, ImageView image, TextView type, ImageView imageSmall) {
            super(itemView);
            this.title = title;
            this.desc = desc;
            this.date = date;
            this.image = image;
            this.type = type;
            this.imageSmall = imageSmall;
        }
    }

    @Override
    public void onClick(View view) {
        NewsViewHolder holder = (NewsViewHolder) view.getTag();
        int position = holder.getAdapterPosition();
        listener.onClick(news.get(position));
    }

    public void setNews(List<News> news) {
        this.news = news;
        //notifyDataSetChanged();
    }

    public void setListener(IOnNewsClickListener listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item_layout, parent, false);
        //View view = View.inflate(parent.getContext(), R.layout.news_item_layout, null);
        TextView title = (TextView) view.findViewById(R.id.item_title);
        TextView desc = (TextView) view.findViewById(R.id.item_dest);
        TextView date = (TextView) view.findViewById(R.id.item_date);
        TextView type = (TextView) view.findViewById(R.id.item_type);
        ImageView img = (ImageView) view.findViewById(R.id.image);
        ImageView imgSmall = (ImageView) view.findViewById(R.id.imageSmall);

        NewsViewHolder viewHolder = new NewsViewHolder(view, title, desc, date, img, type, imgSmall);
        view.setOnClickListener(this);
        view.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        News item = news.get(position);
        newsViewHolder.title.setText(item.getTitle());
        newsViewHolder.desc.setText(item.getDescription());
        newsViewHolder.date.setText(item.getDate() != null ? item.getDate() : "");

        String type = item.getType();
        if (type != null && !type.isEmpty()) {
            newsViewHolder.type.setText(type);
            newsViewHolder.type.setVisibility(View.VISIBLE);
        } else {
            newsViewHolder.type.setVisibility(View.GONE);
        }
        newsViewHolder.desc.setText(item.getDescription());
        // Add a request (in this example, called stringRequest) to your RequestQueue.
        String url = item.getImg();
        if (url != null && !url.isEmpty()) {
            //if (ImageLoaderSingleton.getInstance(null).getImageLoader().get`)
            //
            ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext()).getImageLoader().get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        if (isSmall(response.getBitmap())) {
                            newsViewHolder.imageSmall.setImageBitmap(response.getBitmap());
                            newsViewHolder.image.setVisibility(View.GONE);
                            newsViewHolder.imageSmall.setVisibility(View.VISIBLE);
                        } else {
                            newsViewHolder.image.setImageBitmap(response.getBitmap());
                            newsViewHolder.image.setVisibility(View.VISIBLE);
                            newsViewHolder.imageSmall.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, 1000, 1000, ImageView.ScaleType.CENTER_CROP);
        } else {
            newsViewHolder.image.setVisibility(View.GONE);
            newsViewHolder.imageSmall.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (news == null) {
            return 0;
        } else {
            return news.size();
        }
    }

    private static boolean isSmall(Bitmap bmp){
        int minWidth = 256;
        double minRate = 3. / 4;
        return bmp.getWidth() < minWidth || minRate < ((double) bmp.getHeight() / bmp.getWidth());
    }
}

