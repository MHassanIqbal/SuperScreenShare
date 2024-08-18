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
import com.mhassaniqbal22.supershare.model.Audio;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterAudio extends RecyclerView.Adapter<RecyclerAdapterAudio.ViewHolder> {

    private Context context;
    private List<Audio> audioList;
    private AdapterListener listener;
    private SparseBooleanArray selectedItems;

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlAudio;
        TextView audioTitle, audioSize;
        ImageView audioThumbnail;

        ViewHolder(View itemView) {
            super(itemView);

            rlAudio = (RelativeLayout) itemView.findViewById(R.id.rl_audio);

            audioTitle = (TextView) itemView.findViewById(R.id.title_audio);
            audioSize = (TextView) itemView.findViewById(R.id.size_audio);

            audioThumbnail = (ImageView) itemView.findViewById(R.id.icon_audio);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickSelected(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongSelected(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public RecyclerAdapterAudio(Context context, List<Audio> audioList, AdapterListener listener) {
        this.context = context;
        this.audioList = audioList;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_audio, parent, false);

        return new ViewHolder(itemView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Audio aud = audioList.get(position);

        holder.audioTitle.setText(aud.getTitle());
        holder.audioSize.setHint(String.valueOf(aud.getSize()));

        holder.itemView.setActivated(selectedItems.get(position, false));


        Glide.with(context)
                .load(aud.getIcon())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.audioThumbnail);

        holder.rlAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSelected(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioList.size();
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
        for (i = 0; i<audioList.size(); i++){
            selection(i);
        }
        return i;
    }

    public interface AdapterListener {
        void onClickSelected(int position);

        void onLongSelected(int position);
    }

}