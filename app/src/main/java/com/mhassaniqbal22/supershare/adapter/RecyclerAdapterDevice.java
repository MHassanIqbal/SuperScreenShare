package com.mhassaniqbal22.supershare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.model.Device;

import java.util.List;

public class RecyclerAdapterDevice extends RecyclerView.Adapter<RecyclerAdapterDevice.ViewHolder> {

    private Context context;
    private List<Device> deviceList;
    private AdapterListener listener;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView deviceTitle, tvBuzz, tvCamera, tvSend;
        ImageView deviceIcon;

        ViewHolder(View itemView) {
            super(itemView);

            deviceTitle = (TextView) itemView.findViewById(R.id.tv_device_name);
            tvBuzz = (TextView) itemView.findViewById(R.id.tv_buzz);
            tvCamera = (TextView) itemView.findViewById(R.id.tv_camera);
            tvSend = (TextView) itemView.findViewById(R.id.tv_send);

            deviceIcon = (ImageView) itemView.findViewById(R.id.iv_device_icon);


            tvBuzz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onBuzzClicked();
                }
            });

            tvCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCameraClicked();
                }
            });

            tvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSendClicked();
                }
            });
        }
    }

    public RecyclerAdapterDevice(Context context, List<Device> deviceList, AdapterListener listener) {
        this.context = context;
        this.deviceList = deviceList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_device, parent, false);

        return new ViewHolder(itemView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Device dev = deviceList.get(position);

        holder.deviceTitle.setText(dev.getTitle());

        Glide.with(context)
                .load(dev.getIcon())
                .into(holder.deviceIcon);

        holder.tvBuzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBuzzClicked();
            }
        });

        holder.tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCameraClicked();
            }
        });

        holder.tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSendClicked();
            }
        });

    }


    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public interface AdapterListener {
        void onBuzzClicked();
        void onCameraClicked();
        void onSendClicked();
    }

}