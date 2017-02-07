package ru.nsu.fit.nsuschedule.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.model.News;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

/**
 * Created by Pavel on 19.10.2016.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<News> news;
    private IEventAdapterParent listener;

    public EventAdapter() {
        setHasStableIds(true);
    }

    @Override
    public void onClick(View view) {
        EventViewHolder holder = (EventViewHolder) view.getTag();
        int position = holder.getAdapterPosition();
        listener.onClickNews(news.get(position));
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public void setListener(IEventAdapterParent listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item_layout, parent, false);
        //View view = View.inflate(parent.getContext(), R.layout.news_item_layout, null);
        TextView title = (TextView) view.findViewById(R.id.item_title);
        TextView desc = (TextView) view.findViewById(R.id.item_dest);
        TextView type = (TextView) view.findViewById(R.id.item_date);
        ImageView img = (ImageView) view.findViewById(R.id.image);
        View content = view.findViewById(R.id.content);

        EventViewHolder viewHolder = new EventViewHolder(view, title, desc, img, type, content);
        view.setOnClickListener(this);
        view.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final EventViewHolder eventViewHolder = (EventViewHolder) holder;
        News item = news.get(position);
        eventViewHolder.title.setText(item.getTitle());
        eventViewHolder.desc.setText(item.getDescription());
        eventViewHolder.type.setText(item.getDate() != null ? item.getDate() : "");

        eventViewHolder.desc.setText(item.getDescription());
        // Add a request (in this example, called stringRequest) to your RequestQueue.
        String url = item.getImg();

        final long id = holder.getItemId();//getItemId(position);
        if (url != null && !url.isEmpty()) {

            ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext()).getImageLoader().get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                        listener.onLoadedImg(position, id, bitmap, eventViewHolder);
                    }

                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, 1000, 1000, ImageView.ScaleType.CENTER_CROP);
        } else {
            eventViewHolder.image.setVisibility(View.GONE);
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

    public interface IEventAdapterParent {
        void onClickNews(News news);

        void onLoadedImg(int pos, long id, Bitmap bmp, EventViewHolder holder);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView desc;
        public TextView type;
        public ImageView image;
        public View content;

        public EventViewHolder(View itemView, TextView title, TextView desc, ImageView image,
                               TextView type, View content) {
            super(itemView);
            this.title = title;
            this.desc = desc;
            this.image = image;
            this.type = type;
            this.content = content;
        }
    }

}



