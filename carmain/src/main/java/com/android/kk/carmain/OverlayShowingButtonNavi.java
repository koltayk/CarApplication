package com.android.kk.carmain;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.kk.carapplication.OverlayShowingButton;

import java.util.List;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonNavi extends OverlayShowingButton {

    public static final String PACKAGE_NAME_PRIMO = "com.nng.igoprimoisr.javaclient,primo";
    public static final String PACKAGE_NAME_AVIC = "jp.pioneer.mbg.avicsync,avic";
    public static final String PACKAGE_NAME_NG_ISR = "com.nng.igoprimoisrael.javaclient,isr";
    public static final String PACKAGE_NAME_NG_BASAR = "com.basarsoft.igonextgen.javaclient,basar";
    public static final String PACKAGE_NAME_SYGIC = "com.sygic.aura,sygic";

    public OverlayShowingButtonNavi() {
        super("navi", 610, 500, new String[]{
                PACKAGE_NAME_AVIC,
//                PACKAGE_NAME_NG_ISR,
                PACKAGE_NAME_PRIMO,
//                PACKAGE_NAME_NG_BASAR,
                PACKAGE_NAME_SYGIC,
        });
    }

    @Override
    public boolean openApp() {
        super.openApp();
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
