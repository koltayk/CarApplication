package com.android.kk.carmain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.android.kk.carapplication.LongClick;
import com.android.kk.carapplication.MainActivity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class CarMainActivity extends MainActivity {
    private static final String MOUNT_SD = "/data/misc/user/bin/mountExtSD.sh1";

//    private ThinBTHFPClient thinBTClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.TAG, "CarMainActivity onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_main);
        requestSystemAlertPermission(CarMainActivity.this,REQUEST_CODE);

        try {

//            Process process = Runtime.getRuntime().exec("su");
//            process.getOutputStream().write(MOUNT_SD.getBytes());
//            Scanner s = new Scanner(process.getInputStream());
//            while(s.hasNextLine()){
//                Log.d(MainActivity.TAG, s.nextLine()); // will give the process output
//            }
//            String cmdRet = MainActivity.mountSda();  // /dev/block/sda csatolása

        } catch (Exception e) {
            e.printStackTrace();
        }
        startService(OverlayShowingButtonServiceZene.class);
        startService(OverlayShowingButtonServiceNavi.class);
        startService(OverlayShowingButtonServiceKi.class);

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
        Log.d(MainActivity.TAG, "CarMainActivity onCreate end");
    }

    private void startService(Class<?> serviceClass) {

        /** check if we already  have permission to draw over other apps */
        if (Settings.canDrawOverlays(this)) {
            Intent service = new Intent(getApplicationContext(), serviceClass);
            service.putExtra("main", this);
            Log.d(MainActivity.TAG, "CarMainActivity.this: " + this + " service: " + service.hashCode());
            startService(service);
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        Log.d(MainActivity.TAG, "CarMainActivity onActivityResult begin");
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            startService(OverlayShowingButtonServiceZene.class);
            startService(OverlayShowingButtonServiceNavi.class);
            startService(OverlayShowingButtonServiceKi.class);
        }
        Log.d(MainActivity.TAG, "CarMainActivity onActivityResult end");
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
