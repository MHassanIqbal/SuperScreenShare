package com.mhassaniqbal22.supershare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.model.App;

import java.util.List;

public class RecyclerAdapterApp extends RecyclerView.Adapter<RecyclerAdapterApp.ViewHolder> {

    private Context context;
    private List<App> appList;
    private AdapterListener listener;
    private SparseBooleanArray selectedItems;

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout rlApp;
        TextView appTitle, appSize;
        ImageView appIcon;

        ViewHolder(View itemView) {
            super(itemView);

            rlApp = (LinearLayout) itemView.findViewById(R.id.ll_app);

            appTitle = (TextView) itemView.findViewById(R.id.title_app);
            appSize = (TextView) itemView.findViewById(R.id.size_app);

            appIcon = (ImageView) itemView.findViewById(R.id.icon_app);

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
                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    return true;
                }
            });
        }
    }

    public RecyclerAdapterApp(Context context, List<App> appList, AdapterListener listener) {
        this.context = context;
        this.appList = appList;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_app, parent, false);

        return new ViewHolder(itemView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        App app = appList.get(position);

        holder.appTitle.setText(app.getTitle());
        holder.appSize.setHint(String.valueOf(app.getSize()));

        Glide.with(context)
                .load(app.getIcon())
                .into(holder.appIcon);

        holder.itemView.setActivated(selectedItems.get(position, false));

        holder.rlApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSelected(position);
            }
        });

    }

    public void selection(int position) {
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

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public int selectAll(){
        clearSelections();
        int i;
        for (i = 0; i<appList.size(); i++){
            selection(i);
        }
        return i;
    }

    public interface AdapterListener {
        void onClickSelected(int position);

        void onLongSelected(int position);
    }

}