package com.android.kk.carmain;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonServiceMulti;

public class OverlayShowingButtonServiceNavi extends OverlayShowingButtonServiceMulti {

    public static final String PACKAGE_NAME_PRIMO = "com.nng.igoprimoisr.javaclient";
    public static final String PACKAGE_NAME_AVIC = "jp.pioneer.mbg.avicsync";
    public static final String PACKAGE_NAME_PAL = "com.nng.igo.primong.palestine";
    public static final String PACKAGE_NAME_NG_ISR = "com.nng.igoprimoisrael.javaclient,isr";
    public static final String PACKAGE_NAME_NG_BASAR = "com.basarsoft.igonextgen.javaclient,basar";
    public static final String PACKAGE_NAME_WAZE = "com.waze";
    public static final String PACKAGE_NAME_SYGIC = "com.sygic.aura";
    public static final String MOUNT = "mount -t ext4 /dev/block/sda1 /udisk/";
    //   public static final String MOUNT = "mount -t ext4 /dev/block/mmcblk1p2 /sdcard1/";

    protected void createButtons() {
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceNavi.createButtons() begin");
        buttons.add(new OverlayShowingButton(this, "sygic", 610, 230, PACKAGE_NAME_SYGIC, View.GONE));
        buttons.add(new OverlayShowingButton(this, "waze", 610, 310, PACKAGE_NAME_WAZE, View.GONE));
//        buttons.add(new OverlayShowingButton(this, "primo", 610, 460, PACKAGE_NAME_PRIMO, View.GONE));
        buttons.add(new OverlayShowingButton(this, "pal", 610, 390, PACKAGE_NAME_PAL, View.GONE));
//        buttons.add(new OverlayShowingButton(this, "avic", 610, 470, PACKAGE_NAME_AVIC, View.VISIBLE));
        Log.d(MainActivity.TAG, "OverlayShowingButtonServiceNavi.createButtons() end");
    }

    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(MainActivity.TAG, "Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED");
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
                Log.d(MainActivity.TAG, "LastKnownLocationTime: " + networkTS);
//                setTime(networkTS);
            }

//            Process process = Runtime.getRuntime().exec(MOUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        long eventtime = SystemClock.uptimeMillis();
//        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
//        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
//        sendOrderedBroadcast(downIntent, null);
//
//        Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
//        KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
//        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
//        sendOrderedBroadcast(upIntent, null);

        return super.openApp(overlayShowingButton);
    }
}
