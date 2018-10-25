package com.android.kk.carleft;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.kk.carapplication.OverlayShowingButton;

import java.io.IOException;
import java.util.List;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonNavi extends OverlayShowingButton {

    public static final String PACKAGE_NAME_WAZE = "com.waze, waze";
    public static final String PACKAGE_NAME_PRIMO = "com.nng.igoprimoisr.javaclient, primo";
    public static final String PACKAGE_NAME_PAL = "com.nng.igo.primong.palestine, pal, iGO_Pal";
    public static final String PACKAGE_NAME_AVIC = "jp.pioneer.mbg.avicsync, avic";
    public static final String PACKAGE_NAME_NG_ISR = "com.nng.igoprimoisrael.javaclient, isr";
    public static final String PACKAGE_NAME_NG_BASAR = "com.basarsoft.igonextgen.javaclient, basar";
    public static final String PACKAGE_NAME_SYGIC = "com.sygic.aura, sygic";

    public OverlayShowingButtonNavi() {
        super("navi", 0, 700, 16, 111, new String[]{
                PACKAGE_NAME_PAL,
//                PACKAGE_NAME_NG_ISR,
//                PACKAGE_NAME_NG_BASAR,
                PACKAGE_NAME_WAZE,
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
        List<String> list = locationManager.getProviders(true);
        Log.d("getProviders",""+list);
        try {
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

    protected void onLongClick() {
        String[] appPackageName = this.appPackageNames[this.appInd].split(",");
        if (appPackageName.length > 2) {
            String dirName = appPackageName[2];
            try {
                Process process = Runtime.getRuntime().exec(new String[] { "su", "-c", "'killall " + getAppPackageName() + "'" });
                String igoDir = "/sdcard/" + dirName + "/";
                String profile = "save/profiles/01";
                process = Runtime.getRuntime().exec(new String[] { "su", "-c", "'cp -R " + igoDir + "save." + profile + "/* " + igoDir + profile+ "'" });
//            String inpStream = readFullyAsString(process.getInputStream(), Charset.defaultCharset().name());
//            String errStream = readFullyAsString(process.getErrorStream(), Charset.defaultCharset().name());
//            Log.d("OverlayShowingButtonKi", errStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
