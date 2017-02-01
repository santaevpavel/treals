package ru.nsu.fit.nsuschedule.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.databinding.PlaceItemLayoutBinding;
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
        PlaceItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.place_item_layout, parent, false);
        PlacesViewHolder viewHolder = new PlacesViewHolder(binding.getRoot(), binding);
        binding.getRoot().setOnClickListener(this);
        binding.getRoot().setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final PlacesViewHolder placesViewHolder = (PlacesViewHolder) holder;
        Place item = places.get(position);
        placesViewHolder.binding.itemTitle.setText(item.getTitle());
        placesViewHolder.binding.itemAddress.setText(item.getPlace());
        placesViewHolder.binding.itemPrice.setText(item.getPrice() + " руб");
        placesViewHolder.binding.itemDistance.setText(String.format(Locale.ENGLISH, "~%dм", new Random().nextInt(8) * 100));

        String type = item.getType();
        if (type != null && !type.isEmpty()) {
            type = type.length() > 30 ? type.substring(0, 30) + "..." : type;
            placesViewHolder.binding.itemType.setText(type);
            placesViewHolder.binding.itemType.setVisibility(View.VISIBLE);
        } else {
            placesViewHolder.binding.itemType.setVisibility(View.GONE);
        }

        String url = item.getImg();
        final long id = holder.getItemId();
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
            placesViewHolder.binding.image.setVisibility(View.GONE);
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

        public PlaceItemLayoutBinding binding;

        public PlacesViewHolder(View itemView, PlaceItemLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }

}

