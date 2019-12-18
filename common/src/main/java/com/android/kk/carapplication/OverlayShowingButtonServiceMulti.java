package com.android.kk.carapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonServiceMulti extends LongClick {

    protected List<OverlayShowingButton> buttons = new ArrayList<>();
    protected OverlayShowingButton activeButton;
    protected MainActivity activity;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.onCreate() begin");
        createButtons();
        for (OverlayShowingButton overlayedButton: buttons) {
            Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.onCreate before " + overlayedButton.name + " getVisibility(): " + overlayedButton.button.getVisibility());
            overlayedButton.onCreate(this, (WindowManager) getSystemService(Context.WINDOW_SERVICE));
            Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.onCreate after " + overlayedButton.name + " getVisibility(): " + overlayedButton.button.getVisibility());
        }
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.onCreate() end");
    }

    protected void createButtons() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle extras = intent.getExtras();
//        activity = (MainActivity) extras.get("main");
        activity = MainActivity.activity;
        activity.getMultiButtons().add(this);
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.activity: " + activity + " service: " + intent.hashCode());
        return START_NOT_STICKY;
    }

    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.openApp(button) before " + overlayShowingButton.name + " getVisibility(): " + overlayShowingButton.button.getVisibility());
        if (overlayShowingButton.getButton().getVisibility() == View.GONE) {
            return false;
        }
        activeButton = overlayShowingButton;
        for (OverlayShowingButton button: buttons) {
            Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.openApp(button) for " + button.name + " getVisibility(): " + button.button.getVisibility());

            if (button != overlayShowingButton) {
                button.setVisible(View.GONE);
            }
        }
        overlayShowingButton.openApp(this);
        overlayShowingButton.setVisible(View.VISIBLE);
        if (activity != null) {
            Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.activity: " + activity + " service: " + this);
            for (OverlayShowingButton currButton: activity.getButtons()) {
                currButton.setVisible(View.VISIBLE);
            }
            for (OverlayShowingButtonServiceMulti currMultiButton: activity.getMultiButtons()) {
                currMultiButton.setButton();
            }
        }
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.openApp(button) after " + overlayShowingButton.name + " getVisibility(): " + overlayShowingButton.button.getVisibility());

        return true;
    }

    @Override
    public void openAppOnTouch(OverlayShowingButton overlayShowingButton) {
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.openAppOnTouch(button) " + overlayShowingButton.name + " getVisibility(): " + overlayShowingButton.button.getVisibility());
//        for (OverlayShowingButton button: buttons) {
//            if (button == overlayShowingButton) {
//                OverlayShowingButton.openApp(this, overlayShowingButton);
//                button.setVisible(View.VISIBLE);
//            }
//            else {
//                button.setVisible(View.GONE);
//            }
//        }
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceMulti.onLongClick(): " + v);
        for (OverlayShowingButton button: buttons) {
            button.setVisible(View.VISIBLE);
        }
        return true;
    }

    public void setButton() {
        for (OverlayShowingButton button: buttons) {
            if (button == activeButton) {
                button.setVisible(View.VISIBLE);
            }
            else {
                button.setVisible(View.GONE);
            }
        }
    }
//
//    public void setTime(long time) throws IOException { // nem müködik
//        Runtime.getRuntime().exec("chmod 666 /dev/alarm");
//        SystemClock.setCurrentTimeMillis(time);
//        Runtime.getRuntime().exec("chmod 664 /dev/alarm");
//    }
//
//    public void initialiseLocationListener(android.content.Context context) {
//
//        android.location.LocationManager locationManager = (android.location.LocationManager)
//                context.getSystemService(android.content.Context.LOCATION_SERVICE);
//
//        android.location.LocationListener locationListener = new android.location.LocationListener() {
//
//            public void onLocationChanged(android.location.Location location) {
//
//                String time = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(location.getTime());
//
//                if( location.getProvider().equals(android.location.LocationManager.GPS_PROVIDER))
//                    android.util.Log.d("Location", "Time GPS: " + time); // This is what we want!
//                else
//                    android.util.Log.d("Location", "Time Device (" + location.getProvider() + "): " + time);
//            }
//
//            public void onStatusChanged(String provider, int status, android.os.Bundle extras) {
//            }
//            public void onProviderEnabled(String provider) {
//            }
//            public void onProviderDisabled(String provider) {
//            }
//        };
//
//        if (android.support.v4.content.ContextCompat.checkSelfPermission(context,
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
//            android.util.Log.d("Location", "Incorrect 'uses-permission', requires 'ACCESS_FINE_LOCATION'");
//            return;
//        }
//
////        locationManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
//        locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
//        // Note: To Stop listening use: locationManager.removeUpdates(locationListener)
//    }
}
