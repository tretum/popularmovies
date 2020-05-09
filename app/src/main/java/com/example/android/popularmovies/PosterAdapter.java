package com.example.android.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.model.QueryListEntry;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private List<QueryListEntry> listEntries = new ArrayList<>();

    private PosterAdapterOnClickHandler clickHandler;

    /**
     * Constructor for the poster adapter.
     * @param clickHandler The handler for clicks on the items in the list
     */
    public PosterAdapter(PosterAdapterOnClickHandler clickHandler) {

        this.clickHandler = clickHandler;
    }

    public interface PosterAdapterOnClickHandler{
        void handleGridItemClicked(int movieId);
    }

    @NonNull
    @Override
    public PosterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.grid_poster_item, parent, false);
        return new PosterAdapterViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterAdapterViewHolder holder, int position) {
        QueryListEntry queryListEntry = listEntries.get(position);

        String imageSize = NetworkUtils.getImageSize(NetworkUtils.IMAGE_SIZES.W500);
        String path = NetworkUtils.API_IMAGE_BASE_PATH + imageSize + queryListEntry.getPosterPath();
        int maxWidth = holder.posterThumbnailImageView.getWidth();
        int maxHeight = holder.posterThumbnailImageView.getWidth();

        Picasso.get()
                .load(path)
                .into(holder.posterThumbnailImageView);
    }

    @Override
    public int getItemCount() {
        if(listEntries == null) {
            return 0;
        }
        return listEntries.size();
    }

    public void changeData(List<QueryListEntry> newData) {
        this.listEntries = newData;
        notifyDataSetChanged();
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterThumbnailImageView;

        public PosterAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            posterThumbnailImageView = itemView.findViewById(R.id.iv_grid_poster);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if(adapterPosition < getItemCount()) {
                QueryListEntry queryListEntry = listEntries.get(adapterPosition);
                clickHandler.handleGridItemClicked(queryListEntry.getMovieId());
            }
        }
    }
}
