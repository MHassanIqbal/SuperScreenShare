package com.mhassaniqbal22.supershare.activity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.mhassaniqbal22.supershare.utils.DividerItemDecoration;
import com.mhassaniqbal22.supershare.utils.EmptyRecyclerView;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ClientActivity extends AppCompatActivity implements SalutDataCallback,
        RecyclerAdapterDevice.AdapterListener {

    private KProgressHUD hud;
    private Salut network;
    private WifiManager wifiManager;

    private boolean deviceSupportError = false;
    private boolean isConnected = false;

    private RelativeLayout rlClientMain;
    private RelativeLayout rlClientSub;

    private RippleBackground rippleBackground;
    private RelativeLayout rlClient;
    private TextView tvRandom;
    private TextView tvTop, tvCentre2;

    private TextView[] all;
    private Random random;

    private List<SalutDevice> salutDeviceList;
    private String[] deviceNameArray;
    private SalutDevice[] deviceArray;
    private SalutDevice salutDevice;

    private RelativeLayout rlHost;
    private EmptyRecyclerView recyclerView;
    private List<Device> deviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        rlClientMain = (RelativeLayout) findViewById(R.id.rl_client_main);
        rlClientSub = (RelativeLayout) findViewById(R.id.rl_client_sub);

        rlClient = (RelativeLayout) findViewById(R.id.rl_client);
        rippleBackground = (RippleBackground) findViewById(R.id.content);

        tvTop = (TextView) findViewById(R.id.tv_top);
        tvCentre2 = (TextView) findViewById(R.id.tv_centre);

        deviceList = new ArrayList<>();


        ImageView ivCancel = (ImageView) findViewById(R.id.iv_cancel_2);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (EmptyRecyclerView) findViewById(R.id.recycler_view_device);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        TextView tv1 = (TextView) findViewById(R.id.tv_1);
        TextView tv2 = (TextView) findViewById(R.id.tv_2);
        TextView tv3 = (TextView) findViewById(R.id.tv_3);
        TextView tv4 = (TextView) findViewById(R.id.tv_4);
        TextView tv5 = (TextView) findViewById(R.id.tv_5);

        all = new TextView[]{tv1, tv2, tv3, tv4, tv5};
        random = new Random();

        salutDeviceList = new ArrayList<>();

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait")
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

                        SalutDataReceiver dataReceiver = new SalutDataReceiver(ClientActivity.this,
                                ClientActivity.this);
                        SalutServiceData serviceData = new SalutServiceData("service", 8888,
                                "Client");

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

        rippleBackground.startRippleAnimation();

        network.discoverWithTimeout(new SalutCallback() {
            @Override
            public void call() {
                salutDeviceList.clear();
                salutDeviceList.addAll(network.foundDevices);

                deviceNameArray = new String[network.foundDevices.size()];
                deviceArray = new SalutDevice[network.foundDevices.size()];

                int index = 0;
                for (SalutDevice device : network.foundDevices) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;

                    tvRandom = all[random.nextInt(all.length)];
                    tvRandom.setVisibility(View.VISIBLE);
                    tvRandom.setText(deviceNameArray[index]);

                    salutDevice = deviceArray[index];

                    index++;
                }

            }
        }, new SalutCallback() {
            @Override
            public void call() {

                rippleBackground.stopRippleAnimation();

                Snackbar snackbar = Snackbar
                        .make(rlClient, "Bummer, we didn't find anyone. ", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                success();
                            }
                        });

                snackbar.show();

            }
        }, 30000);
    }

    public void onTvClick(View view) {
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {

                        network.registerWithHost(salutDevice, new SalutCallback() {
                            @Override
                            public void call() {
                                isConnected = true;
                            }
                        }, new SalutCallback() {
                            @Override
                            public void call() {
                                isConnected = false;
                            }
                        });

                        try {
                            Thread.sleep(3000);
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
                        if (isConnected) {
                            successfullyConnected();
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(rlClient, "Filed to Connect: " + salutDevice.readableName, Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Restart", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            tvRandom.setVisibility(View.GONE);
                                            success();
                                        }
                                    });

                            snackbar.show();
                        }
                    }
                }).create().start();
    }

    private void successfullyConnected() {
        rlClientMain.setVisibility(View.GONE);
        rlClientSub.setVisibility(View.VISIBLE);

        deviceList.add(new Device(R.drawable.man, salutDevice.deviceName));
        RecyclerAdapterDevice adapterDevice = new RecyclerAdapterDevice(this, deviceList, this);
        recyclerView.setAdapter(adapterDevice);

        tvTop.setText(network.thisDevice.deviceName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        network.unregisterClient(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        network.unregisterClient(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        network.unregisterClient(false);
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

            Toast.makeText(this, salutDevice.deviceName + " just buzzed you", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBuzzClicked() {
        network.sendToHost("buzz", new SalutCallback() {
            @Override
            public void call() {
                Snackbar.make(rlClient, "Oh no! The data failed to send.", Snackbar.LENGTH_INDEFINITE).show();
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
