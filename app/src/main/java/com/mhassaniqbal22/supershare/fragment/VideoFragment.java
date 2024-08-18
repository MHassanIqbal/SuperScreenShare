package com.mhassaniqbal22.supershare.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.adapter.RecyclerAdapterVideo;
import com.mhassaniqbal22.supershare.listener.RecyclerClickListener;
import com.mhassaniqbal22.supershare.model.Video;
import com.mhassaniqbal22.supershare.utils.DividerItemDecoration;
import com.mhassaniqbal22.supershare.utils.EmptyRecyclerView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment implements RecyclerAdapterVideo.AdapterListener {

    private List<Video> videoList;

    private String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};

    private RecyclerClickListener listener;

    public static RecyclerAdapterVideo adapterVid;

    public VideoFragment() {
        //empty constructor required
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        videoList  = new ArrayList<>();

        listener = (RecyclerClickListener) getActivity();

        EmptyRecyclerView recyclerView = (EmptyRecyclerView) rootView.findViewById(R.id.recycler_view_empty);
        adapterVid = new RecyclerAdapterVideo(getActivity(), videoList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterVid);

        fetchVideos();

        return rootView;
    }


    private void fetchVideos() {
        videoList.clear();

        externalVideos();
        internalVideos();
    }

    private void externalVideos() {
        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media.DATA}, null, null,
                MediaStore.Video.Media.DEFAULT_SORT_ORDER + " DESC");


        int count = mCursor.getCount();

        String[] videos = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                videos[i] = mCursor.getString(0);

                File file = new File(videos[i]);

                String s = file.getName();
                String name = s.substring(0, s.lastIndexOf("."));

                long size = file.length();
                int digitGroup = (int) (Math.log10(size) / Math.log10(1024));
                String totalSize = new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroup))
                        + " " + units[digitGroup];

                videoList.add(new Video(name, file.getPath(), totalSize));
                i++;
            } while (mCursor.moveToNext());
        }
    }

    private void internalVideos() {
        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media.DATA}, null, null,
                MediaStore.Video.Media.DEFAULT_SORT_ORDER + " DESC");

        int count = mCursor.getCount();

        String[] videos = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                videos[i] = mCursor.getString(0);

                File file = new File(videos[i]);

                String s = file.getName();
                String name = s.substring(0, s.lastIndexOf("."));

                long size = file.length();
                int digitGroup = (int) (Math.log10(size) / Math.log10(1024));
                String totalSize = new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroup))
                        + " " + units[digitGroup];

                videoList.add(new Video(name, file.getPath(), totalSize));
                i++;
            } while (mCursor.moveToNext());
        }
    }

    @Override
    public void onClickSelected(int position) {
        adapterVid.selection(position);
        listener.onMyClick(3, adapterVid.getSelectedItemCount());
    }

    @Override
    public void onLongSelected(int position) {

    }
}
