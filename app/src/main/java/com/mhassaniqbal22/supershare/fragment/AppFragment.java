package com.mhassaniqbal22.supershare.fragment;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.adapter.RecyclerAdapterApp;
import com.mhassaniqbal22.supershare.listener.RecyclerClickListener;
import com.mhassaniqbal22.supershare.model.App;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AppFragment extends Fragment implements RecyclerAdapterApp.AdapterListener {

    private List<App> appList;

    private String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};

    private RecyclerClickListener listener;

    public static RecyclerAdapterApp adapterApp;

    public AppFragment() {
        //empty constructor required
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);

        appList = new ArrayList<>();

        listener = (RecyclerClickListener) getActivity();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapterApp = new RecyclerAdapterApp(getActivity(), appList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(adapterApp);

        fetchApps();

        return rootView;
    }

    private void fetchApps() {
        appList.clear();
        installedApps();
//        systemApps();

    }

    public void installedApps() {
        List<PackageInfo> PackList = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < PackList.size(); i++) {
            PackageInfo packInfo = PackList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = packInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                String path = packInfo.applicationInfo.sourceDir;
                String packageName = packInfo.packageName;
                Drawable icon = packInfo.applicationInfo.loadIcon(getActivity().getPackageManager());
                long size = new File(packInfo.applicationInfo.sourceDir).length();

                int digitGroup = (int) (Math.log10(size) / Math.log10(1024));
                String totalSize = new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroup))
                        + " " + units[digitGroup];

                appList.add(new App(appName, icon, totalSize, path));
            }
        }

    }

    public void systemApps() {
        List<PackageInfo> PackList = getActivity().getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < PackList.size(); i++) {
            PackageInfo packInfo = PackList.get(i);

            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                String appName = packInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                String path = packInfo.applicationInfo.sourceDir;
                String packageName = packInfo.packageName;
                Drawable icon = packInfo.applicationInfo.loadIcon(getActivity().getPackageManager());
                long size = new File(packInfo.applicationInfo.sourceDir).length();

                int digitGroup = (int) (Math.log10(size) / Math.log10(1024));
                String totalSize = new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroup))
                        + " " + units[digitGroup];

                appList.add(new App(appName, icon, totalSize, path));

            }
        }
    }

    @Override
    public void onClickSelected(int position) {
        adapterApp.selection(position);
        listener.onMyClick(1, adapterApp.getSelectedItemCount());
    }

    @Override
    public void onLongSelected(int position) {

    }
}
