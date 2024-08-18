package com.mhassaniqbal22.supershare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.model.Video;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterVideo extends RecyclerView.Adapter<RecyclerAdapterVideo.ViewHolder> {

    private Context context;
    private List<Video> videoList;
    private AdapterListener listener;
    private SparseBooleanArray selectedItems;

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlVideo;
        TextView videoTitle, videoSize;
        ImageView videoThumbnail;

        ViewHolder(View itemView) {
            super(itemView);

            rlVideo = (RelativeLayout) itemView.findViewById(R.id.rl_video);

            videoTitle = (TextView) itemView.findViewById(R.id.title_video);
            videoSize = (TextView) itemView.findViewById(R.id.size_video);

            videoThumbnail = (ImageView) itemView.findViewById(R.id.icon_video);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    // send selected click in callback
                    listener.onClickSelected(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //send selected long clock in callback
                    listener.onLongSelected(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public RecyclerAdapterVideo(Context context, List<Video> videoList, AdapterListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_video, parent, false);

        return new ViewHolder(itemView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Video vid = videoList.get(position);

        holder.videoTitle.setText(vid.getTitle());
        holder.videoSize.setHint(String.valueOf(vid.getSize()));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.icon);
        requestOptions.error(R.drawable.icon);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(vid.getPath())
                .into(holder.videoThumbnail);

        holder.itemView.setActivated(selectedItems.get(position, false));

        holder.rlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void selection(int position){
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public int selectAll(){
        clearSelections();
        int i;
        for (i = 0; i<videoList.size(); i++){
            selection(i);
        }
        return i;
    }
    public interface AdapterListener {
        void onClickSelected(int position);

        void onLongSelected(int position);
    }

}