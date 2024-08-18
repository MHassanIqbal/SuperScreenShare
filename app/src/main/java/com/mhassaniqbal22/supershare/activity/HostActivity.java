package com.mhassaniqbal22.supershare.activity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mhassaniqbal22.supershare.R;
import com.mhassaniqbal22.supershare.adapter.RecyclerAdapterDevice;
import com.mhassaniqbal22.supershare.model.Device;
import com.mhassaniqbal22.supershare.model.Image;
import com.mhassaniqbal22.supershare.utils.DividerItemDecoration;
import com.mhassaniqbal22.supershare.utils.EmptyRecyclerView;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;
import java.util.List;

public class HostActivity extends AppCompatActivity implements
        SalutDataCallback, SalutDeviceCallback, RecyclerAdapterDevice.AdapterListener {

    private KProgressHUD hud;
    private Salut network;
    private WifiManager wifiManager;

    private boolean deviceSupportError = false;

    private RelativeLayout rlHost;
    private EmptyRecyclerView recyclerView;
    private TextView tvTop;

    private List<Device> deviceList;
    private SalutDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        rlHost = (RelativeLayout) findViewById(R.id.rl_host);
        ImageView ivCancel = (ImageView) findViewById(R.id.iv_cancel_2);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTop = (TextView) findViewById(R.id.tv_top);

        device = new SalutDevice();

        deviceList = new ArrayList<>();

        recyclerView = (EmptyRecyclerView) findViewById(R.id.recycler_view_device);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Creating Group")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {

                        if (!wifiManager.isWifiEnabled()) {
                            wifiManager.setWifiEnabled(true);
                        }

                        SalutDataReceiver dataReceiver = new SalutDataReceiver(HostActivity.this,
                                HostActivity.this);
                        SalutServiceData serviceData = new SalutServiceData("service", 8888,
                                "Host");

                        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
                            @Override
                            public void call() {
                                deviceSupportError = true;
                            }
                        }) {
                            @Override
                            public String serialize(Object o) {
                                return null;
                            }
                        };

                        network.startNetworkService(HostActivity.this);

                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        hud.dismiss();
                        if (deviceSupportError) {
                            deviceErrorToast();
                        } else {
                            success();
                        }
                    }
                }).create().start();

    }

    private void deviceErrorToast() {
        Toast.makeText(this, "Sorry, but this device does not support WiFi Direct.",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void success() {

        Snackbar.make(rlHost, "Group Created Successfully", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(rlHost, "Waiting for devices to connect...", Snackbar.LENGTH_INDEFINITE).show();
            }
        }, 2000);

        tvTop.setText(network.thisDevice.deviceName);
    }

    @Override
    public void call(SalutDevice device) {
        Snackbar.make(rlHost, device.deviceName + " is connected", Snackbar.LENGTH_SHORT).show();

        deviceList.add(new Device(R.drawable.man, device.deviceName));
        RecyclerAdapterDevice adapterDevice = new RecyclerAdapterDevice(this, deviceList, this);
        recyclerView.setAdapter(adapterDevice);

        this.device = device;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        network.stopNetworkService(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        network.stopNetworkService(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        network.stopNetworkService(false);
    }


    @Override
    public void onDataReceived(Object data) {
        if (data.toString().equals("buzz")) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
            Toast.makeText(this,  device.deviceName + " just buzzed you", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBuzzClicked() {
        network.sendToAllDevices("buzz", new SalutCallback() {

            @Override
            public void call() {
                Snackbar.make(rlHost, "Oh no! The data failed to send.", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    @Override
    public void onCameraClicked() {

    }

    @Override
    public void onSendClicked() {

    }
}
