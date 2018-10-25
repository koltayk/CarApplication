package com.android.kk.carmain;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.android.kk.carapplication.GetGPSTimeThread;
import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.R;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarMainActivity extends MainActivity implements Serializable {

//    private ThinBTHFPClient thinBTClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_car_main);
        requestSystemAlertPermission(CarMainActivity.this,1);

//        if (isSystemAlertPermissionGranted(CarMainActivity.this)) {

//        setFullScreen();
        Intent serviceZene = new Intent(getApplicationContext(), OverlayShowingButtonZene.class);
        startService(serviceZene);
        serviceZene.putExtra("main", this);
        Intent serviceNavi = new Intent(getApplicationContext(), OverlayShowingButtonNavi.class);
        startService(serviceNavi);
        serviceNavi.putExtra("main", this);
        startService(new Intent(getApplicationContext(), OverlayShowingButtonKi.class));

//        Intent serviceBT = new Intent(getApplicationContext(), ThinBTHFPClient.class);
//        startService(serviceBT);

//            overlayShowingService();
//        }

        getTimeAndPos();
//
//        thinBTClient = new ThinBTClient(this);
//
//        if(thinBTClient.disabled) {
//            thinBTClient.connect();
//        }
//
//        thinBTClient.ring();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            if (hasFocus) {
//                setFullScreen();
//            }
//        }
    }
//
//    private void setFullScreen() {
//        getWindow().getDecorView().setSystemUiVisibility(
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//            View.SYSTEM_UI_FLAG_FULLSCREEN |
//            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        );
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (igo != null) {
//            igo.onDestroy();
//        }
//        if (gonemad != null) {
//            gonemad.onDestroy();
//        }
//        finishAffinity();
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        thinBTClient.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        thinBTClient.onPause();
//    }
}
