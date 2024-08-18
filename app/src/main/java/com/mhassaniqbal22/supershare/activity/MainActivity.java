package com.mhassaniqbal22.supershare.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.fragment.AppFragment;
import com.mhassaniqbal22.supershare.fragment.AudioFragment;
import com.mhassaniqbal22.supershare.fragment.ImageFragment;
import com.mhassaniqbal22.supershare.fragment.VideoFragment;
import com.mhassaniqbal22.supershare.adapter.ViewPagerAdapter;
import com.mhassaniqbal22.supershare.listener.RecyclerClickListener;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        RecyclerClickListener, View.OnClickListener {

    private RelativeLayout rlHeadDefault, rlHeadSend;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout.LayoutParams layoutParams;
    private TextView tvCount;
    private CheckBox cbSelectAll;

    private boolean isClicked = false;
    private int count = 1;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_bar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        layoutParams = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();

        rlHeadDefault = (RelativeLayout) findViewById(R.id.rl_head_default);
        rlHeadSend = (RelativeLayout) findViewById(R.id.rl_head_send);

        tvCount = (TextView) findViewById(R.id.tv_count);

        ImageView ivCancel = (ImageView) findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(this);

        cbSelectAll = (CheckBox) findViewById(R.id.cb_all);
        cbSelectAll.setOnClickListener(this);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AppFragment(), "App");
        adapter.addFragment(new ImageFragment(), "Image");
        adapter.addFragment(new VideoFragment(), "Video");
        adapter.addFragment(new AudioFragment(), "Audio");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
        if (isClicked) {
            changeHeadUi(false);
        }
        cbSelectAll.setChecked(false);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void changeHeadUi(boolean b) {
        if (b) {
            rlHeadDefault.setVisibility(View.GONE);
            rlHeadSend.setVisibility(View.VISIBLE);

            layoutParams.setScrollFlags(0);
            collapsingToolbar.setLayoutParams(layoutParams);

            tvCount.setText(String.valueOf(count));

            isClicked = true;
        } else {

            rlHeadDefault.setVisibility(View.VISIBLE);
            rlHeadSend.setVisibility(View.GONE);

            layoutParams.setScrollFlags(1);
            collapsingToolbar.setLayoutParams(layoutParams);

            switch (index) {
                case 1:
                    AppFragment.adapterApp.clearSelections();
                    break;

                case 2:
                    ImageFragment.adapterImg.clearSelections();
                    break;

                case 3:
                    VideoFragment.adapterVid.clearSelections();
                    break;

                case 4:
                    AudioFragment.adapterAud.clearSelections();
                    break;
            }

            isClicked = false;
        }
    }

    @Override
    public void onMyClick(int index, int position) {
        this.index = index;
        count = position;

        if (position == 0) {
            changeHeadUi(false);
        } else {
            changeHeadUi(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                changeHeadUi(false);
                break;

            case R.id.cb_all:
                switch (index) {
                    case 1:
                        if (cbSelectAll.isChecked()) {
                            AppFragment.adapterApp.selectAll();
                            tvCount.setText(String.valueOf(AppFragment.adapterApp.getSelectedItemCount()));
                        } else {
                            AppFragment.adapterApp.clearSelections();
                            tvCount.setText(String.valueOf(AppFragment.adapterApp.getSelectedItemCount()));
                        }

                        count = AppFragment.adapterApp.getSelectedItemCount();
                        break;

                    case 2:
                        if (cbSelectAll.isChecked()) {
                            ImageFragment.adapterImg.selectAll();
                            tvCount.setText(String.valueOf(ImageFragment.adapterImg.getSelectedItemCount()));

                        } else {
                            ImageFragment.adapterImg.clearSelections();
                            tvCount.setText(String.valueOf(ImageFragment.adapterImg.getSelectedItemCount()));
                        }
                        count = ImageFragment.adapterImg.getSelectedItemCount();
                        break;

                    case 3:
                        if (cbSelectAll.isChecked()) {
                            VideoFragment.adapterVid.selectAll();
                            tvCount.setText(String.valueOf(VideoFragment.adapterVid.getSelectedItemCount()));

                        } else {
                            VideoFragment.adapterVid.clearSelections();
                            tvCount.setText(String.valueOf(VideoFragment.adapterVid.getSelectedItemCount()));
                        }
                        count = VideoFragment.adapterVid.getSelectedItemCount();
                        break;

                    case 4:
                        if (cbSelectAll.isChecked()) {
                            AudioFragment.adapterAud.selectAll();
                            tvCount.setText(String.valueOf(AudioFragment.adapterAud.getSelectedItemCount()));

                        } else {
                            AudioFragment.adapterAud.clearSelections();
                            tvCount.setText(String.valueOf(AudioFragment.adapterAud.getSelectedItemCount()));
                        }
                        count = AudioFragment.adapterAud.getSelectedItemCount();
                        break;
                }

                break;
        }
    }

    public void onShareClick(View view) {
        if (count > 0) {
            startActivity(new Intent(MainActivity.this, HostActivity.class));
        } else {
            Toast.makeText(this, "Select Something to Share", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSendClick(View view) {

        startActivity(new Intent(MainActivity.this, HostActivity.class));
    }

    public void onReceiveClick(View view) {
        startActivity(new Intent(MainActivity.this, ClientActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (!isClicked) {
            super.onBackPressed();
        } else {
            changeHeadUi(false);
        }
    }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
        }
}
