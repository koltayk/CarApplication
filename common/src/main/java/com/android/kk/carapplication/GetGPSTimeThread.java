package com.android.kk.carapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by kk on 2017.10.29.
 */
public class GetGPSTimeThread implements Runnable {
    private static final int REQUEST_LOCATION = 0;
    private final LocationManager locationManager;
    private MainActivity activity;
    private ScheduledExecutorService stp;

    public GetGPSTimeThread(MainActivity activity, ScheduledExecutorService stp, LocationManager locationManager) {
        this.activity = activity;
        this.stp = stp;
        this.locationManager = locationManager;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
    }

    private void processCommand() {
        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Check Permissions Now
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        List<String> list = locationManager.getProviders(true);
        Log.d("getProviders", "" + list);
        try {

//            initialiseLocationListener(getApplicationContext());

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {
                long networkTS = lastKnownLocation.getTime();
                Runtime.getRuntime().exec("busybox date -s @" + networkTS / 1000);
                Log.d(MainActivity.TAG, "LastKnownLocationTime: " + new Date(networkTS));
                this.activity.setLatitude(lastKnownLocation.getLatitude());
                this.activity.setLongitude(lastKnownLocation.getLongitude());
                stp.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}
