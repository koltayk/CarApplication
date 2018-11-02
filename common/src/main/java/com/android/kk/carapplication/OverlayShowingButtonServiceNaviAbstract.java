package com.android.kk.carapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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

public class OverlayShowingButtonServiceNaviAbstract extends LongClick {

    protected List<OverlayShowingButton> buttons = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        createButtons();
        for (OverlayShowingButton overlayedButton: buttons) {
            Log.d("kkLog", "OverlayShowingButtonServiceNaviAbstract.onCreate before " + overlayedButton.name + " getVisibility(): " + overlayedButton.button.getVisibility());
            overlayedButton.onCreate(this, (WindowManager) getSystemService(Context.WINDOW_SERVICE));
            Log.d("kkLog", "OverlayShowingButtonServiceNaviAbstract.onCreate after " + overlayedButton.name + " getVisibility(): " + overlayedButton.button.getVisibility());

        }
    }

    protected void createButtons() {
    }

    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        Log.d("kkLog", "OverlayShowingButtonServiceNaviAbstract.openApp(button) before " + overlayShowingButton.name + " getVisibility(): " + overlayShowingButton.button.getVisibility());
    if (overlayShowingButton.getButton().getVisibility() == View.GONE) {
            return false;
    }
        for (OverlayShowingButton button: buttons) {
            Log.d("kkLog", "OverlayShowingButtonServiceNaviAbstract.openApp(button) for " + button.name + " getVisibility(): " + button.button.getVisibility());

            if (button != overlayShowingButton) {
                button.setVisible(View.GONE);
            }
        }
        OverlayShowingButton.openApp(this, overlayShowingButton);
        overlayShowingButton.setVisible(View.VISIBLE);
        final MainActivity activity = MainActivity.activity;
        for (OverlayShowingButton currButton: activity.getButtons()) {
            currButton.setVisible(View.VISIBLE);
        }
        Log.d("kkLog", "OverlayShowingButtonServiceNaviAbstract.openApp(button) after " + overlayShowingButton.name + " getVisibility(): " + overlayShowingButton.button.getVisibility());

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
//        List<String> list = locationManager.getProviders(true);
//        Log.d("getProviders",""+list);
        try {

//            initialiseLocationListener(getApplicationContext());

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            if (lastKnownLocation != null) {
                long networkTS = lastKnownLocation.getTime();
                Runtime.getRuntime().exec("busybox date -s @" + networkTS/1000);
                Log.d("kkLog", "LastKnownLocationTime: " + networkTS);
//                setTime(networkTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void openAppOnTouch(OverlayShowingButton overlayShowingButton) {
        Log.d("kkLog", "OverlayShowingButtonServiceNaviAbstract.openAppOnTouch(button) " + overlayShowingButton.name + " getVisibility(): " + overlayShowingButton.button.getVisibility());
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
        Log.d("kkLog", "OverlayShowingButtonServiceNaviAbstract.onLongClick(): " + v);
        for (OverlayShowingButton button: buttons) {
            button.setVisible(View.VISIBLE);
        }
        return false;
    }

    public void setButton(OverlayShowingButton overlayShowingButton) {
        for (OverlayShowingButton button: buttons) {
            if (button == overlayShowingButton) {
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
