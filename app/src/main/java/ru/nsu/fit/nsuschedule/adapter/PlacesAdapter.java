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
import ru.nsu.fit.nsuschedule.model.Place;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;

/**
 * Created by Pavel on 19.10.2016.
 */
public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public static final int MAX_LENGTH_DESCRIPTION = 200;
    private List<Place> places;
    private IPlacesAdapterParent listener;

    public PlacesAdapter() {
        setHasStableIds(true);
    }

    @Override
    public void onClick(View view) {
        PlacesViewHolder holder = (PlacesViewHolder) view.getTag();
        int position = holder.getAdapterPosition();
        listener.onClickPlace(places.get(position));
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
        //notifyDataSetChanged();
    }

    public void setListener(IPlacesAdapterParent listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        PlacesViewHolder viewHolder = new PlacesViewHolder(view, title, desc, date, img, type, imgSmall);
        view.setOnClickListener(this);
        view.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final PlacesViewHolder placesViewHolder = (PlacesViewHolder) holder;
        Place item = places.get(position);
        placesViewHolder.title.setText(item.getTitle());
        placesViewHolder.date.setText(item.getPlace());

        String type = item.getType();
        if (type != null && !type.isEmpty()) {
            type = type.length() > 30 ? type.substring(0, 30) + "..." : type;
            placesViewHolder.type.setText(type);
            placesViewHolder.type.setVisibility(View.VISIBLE);
        } else {
            placesViewHolder.type.setVisibility(View.GONE);
        }
        String description = item.getDescription();
        description = description.length() > MAX_LENGTH_DESCRIPTION
                ? description.substring(0, MAX_LENGTH_DESCRIPTION) + "..." : description;
        placesViewHolder.desc.setText(description);
        // Add a request (in this example, called stringRequest) to your RequestQueue.
        String url = item.getImg();

        final long id = holder.getItemId();//getItemId(position);
        if (url != null && !url.isEmpty()) {

            ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext()).getImageLoader().get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                        listener.onLoadedImg(position, id, bitmap, placesViewHolder);
                    }

                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }, 1000, 1000, ImageView.ScaleType.CENTER_CROP);
        } else {
            placesViewHolder.image.setVisibility(View.GONE);
            placesViewHolder.imageSmall.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (places == null) {
            return 0;
        } else {
            return places.size();
        }
    }

    public interface IPlacesAdapterParent {
        void onClickPlace(Place place);

        void onLoadedImg(int pos, long id, Bitmap bmp, PlacesViewHolder holder);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView desc;
        public TextView date;
        public TextView type;
        public ImageView image;
        public ImageView imageSmall;

        public PlacesViewHolder(View itemView, TextView title,
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

}

