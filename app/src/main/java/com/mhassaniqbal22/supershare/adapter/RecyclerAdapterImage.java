package com.mhassaniqbal22.supershare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.model.Image;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterImage extends RecyclerView.Adapter<RecyclerAdapterImage.ViewHolder> {

    private Context context;
    private List<Image> imageList;
    private AdapterListener listener;
    private SparseBooleanArray selectedItems;

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlImage;
        ImageView image;
        CheckBox cbImg;

        ViewHolder(View itemView) {
            super(itemView);

            rlImage = (RelativeLayout) itemView.findViewById(R.id.rl_image);

            image = (ImageView) itemView.findViewById(R.id.icon_image);
            cbImg = (CheckBox) itemView.findViewById(R.id.cb_img);

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

    public RecyclerAdapterImage(Context context, List<Image> imageList, AdapterListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_image, parent, false);

        return new ViewHolder(itemView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Image img = imageList.get(position);

        Glide.with(context)
                .load(img.getPath())
                .into(holder.image);

        holder.itemView.setActivated(selectedItems.get(position, false));

        holder.rlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
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
        for (i = 0; i<imageList.size(); i++){
            selection(i);
        }
        return i;
    }

    public interface AdapterListener {
        void onClickSelected(int position);

        void onLongSelected(int position);
    }
}