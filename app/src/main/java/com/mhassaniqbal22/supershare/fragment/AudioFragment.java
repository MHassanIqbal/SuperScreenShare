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
import com.mhassaniqbal22.supershare.adapter.RecyclerAdapterAudio;
import com.mhassaniqbal22.supershare.listener.RecyclerClickListener;
import com.mhassaniqbal22.supershare.model.Audio;
import com.mhassaniqbal22.supershare.utils.DividerItemDecoration;
import com.mhassaniqbal22.supershare.utils.EmptyRecyclerView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AudioFragment extends Fragment implements RecyclerAdapterAudio.AdapterListener {

    private List<Audio> audioList;

    private String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};

    private RecyclerClickListener listener;

    public static RecyclerAdapterAudio adapterAud;

    public AudioFragment() {
        //empty constructor required
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        audioList = new ArrayList<>();

        listener = (RecyclerClickListener) getActivity();

        EmptyRecyclerView recyclerView = (EmptyRecyclerView) rootView.findViewById(R.id.recycler_view_empty);
        adapterAud = new RecyclerAdapterAudio(getActivity(), audioList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterAud);

        fetchAudio();

        return rootView;
    }

    private void fetchAudio() {
        audioList.clear();

        externalAudio();
        internalAudio();
    }

    private void externalAudio() {
        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DATA}, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER + " DESC");


        int count = mCursor.getCount();

        String[] audios = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                audios[i] = mCursor.getString(0);

                File file = new File(audios[i]);

                String s = file.getName();
                String name = s.substring(0, s.lastIndexOf("."));

                long size = file.length();
                int digitGroup = (int) (Math.log10(size) / Math.log10(1024));
                String totalSize = new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroup))
                        + " " + units[digitGroup];

                audioList.add(new Audio(name, R.drawable.icon, totalSize, file.getPath()));
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

        String[] audios = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                audios[i] = mCursor.getString(0);

                File file = new File(audios[i]);

                String s = file.getName();
                String name = s.substring(0, s.lastIndexOf("."));

                long size = file.length();
                int digitGroup = (int) (Math.log10(size) / Math.log10(1024));
                String totalSize = new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroup))
                        + " " + units[digitGroup];

                audioList.add(new Audio(name, R.drawable.icon, totalSize, file.getPath()));
                i++;
            } while (mCursor.moveToNext());
        }
    }

    @Override
    public void onClickSelected(int position) {
        adapterAud.selection(position);
        listener.onMyClick(4, adapterAud.getSelectedItemCount());
    }

    @Override
    public void onLongSelected(int position) {

    }
}
