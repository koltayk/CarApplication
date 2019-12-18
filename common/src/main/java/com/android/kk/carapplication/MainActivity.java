package com.android.kk.carapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements Serializable {
    public static final String TAG = "kklog";
//
    /** code to post/handler request for permission */
    public final static int REQUEST_CODE = -1010101;

    public static MainActivity activity;

    private List<OverlayShowingButton> buttons = new ArrayList<>();

    private List<OverlayShowingButtonServiceMulti> multiButtons = new ArrayList<>();

    private double latitude;

    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<OverlayShowingButton> getButtons() {
        return buttons;
    }

    public List<OverlayShowingButtonServiceMulti> getMultiButtons() {
        return multiButtons;
    }

    /**
     * beállítja az időt és lekéri a GPS pozíciót
     */
    public void getTimeAndPos() {
        // idő a gps-től
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        GetGPSTimeThread worker = new GetGPSTimeThread(this, scheduledThreadPool, locationManager);
        scheduledThreadPool.scheduleWithFixedDelay(worker, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //for lollipop devices
    public static void requestSystemAlertPermission(Activity context, int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        final String packageName = context.getPackageName();
        final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName));
        if (context != null && !isSystemAlertPermissionGranted(context))
            context.startActivityForResult(intent, requestCode);
    }

    @TargetApi(23)
    public static boolean isSystemAlertPermissionGranted(Context context) {
        final boolean result = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || Settings.canDrawOverlays(context);
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            startService(data);
        }
    }

    public static String cmd(String sh, String command, boolean ignoreError) throws IOException, InterruptedException {
        Log.d(TAG, command);
        Process process = Runtime.getRuntime().exec(new String[]{sh, "-c", command});
        process.waitFor();
        String errStream = readFullyAsString(process.getErrorStream(), Charset.defaultCharset().name());
        String outStream = readFullyAsString(process.getInputStream(), Charset.defaultCharset().name());
        Log.d(TAG, outStream);
        Log.d(TAG, errStream);
        final int exitValue = process.exitValue();
        if (exitValue != 0 && !ignoreError) {
            throw new RuntimeException("hiba a következő parancsban: " + command + ", hibaüzenet: " + errStream);
        }
        String ret = exitValue + " " + outStream + errStream;
        return ret;
    }

    public static String mountSda() throws IOException, InterruptedException {
        return cmd("su", "/data/misc/user/bin/mountSDA.sh", false);
    }

    public static String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    public static byte[] readFullyAsBytes(InputStream inputStream) throws IOException {
        return readFully(inputStream).toByteArray();
    }

    public static ByteArrayOutputStream readFully(InputStream inputStream)  throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}
