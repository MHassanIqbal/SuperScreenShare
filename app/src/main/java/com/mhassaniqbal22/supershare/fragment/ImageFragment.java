package com.mhassaniqbal22.supershare.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.adapter.RecyclerAdapterImage;
import com.mhassaniqbal22.supershare.listener.RecyclerClickListener;
import com.mhassaniqbal22.supershare.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment implements RecyclerAdapterImage.AdapterListener {

    private List<Image> imageList;

    private RecyclerClickListener listener;

    public static RecyclerAdapterImage adapterImg;

    public ImageFragment() {
        //empty constructor required
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);

        imageList = new ArrayList<>();

        listener = (RecyclerClickListener) getActivity();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapterImg = new RecyclerAdapterImage(getActivity(), imageList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterImg);

        fetchImages();

        return rootView;
    }

    private void fetchImages() {
        imageList.clear();

        externalAudio();
        internalAudio();
    }

    private void externalAudio() {
        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA}, null, null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC");


        int count = mCursor.getCount();

        String[] images = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                images[i] = mCursor.getString(0);
                File file = new File(images[i]);
                imageList.add(new Image(file.getPath()));
                i++;
            } while (mCursor.moveToNext());
        }
    }

    private void internalAudio() {
        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media.DATA}, null, null,
                MediaStore.Video.Media.DEFAULT_SORT_ORDER + " DESC");

        int count = mCursor.getCount();

        String[] images = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                images[i] = mCursor.getString(0);
                File file = new File(images[i]);
                imageList.add(new Image(file.getPath()));
                i++;
            } while (mCursor.moveToNext());
        }
    }

    @Override
    public void onClickSelected(int position) {
        adapterImg.selection(position);
        listener.onMyClick(2, adapterImg.getSelectedItemCount());
    }

    @Override
    public void onLongSelected(int position) {

    }
}
