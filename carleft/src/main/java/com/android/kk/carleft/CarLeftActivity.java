package com.android.kk.carleft;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonService;

import java.io.Serializable;

public class CarLeftActivity extends MainActivity implements Serializable {

    public static CarLeftActivity activity;

    public double latitude;
    public double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_carleft);
        requestSystemAlertPermission(CarLeftActivity.this,1);

        Intent serviceZene = new Intent(getApplicationContext(), OverlayShowingButtonServiceOrux.class);
        startService(serviceZene);
        serviceZene.putExtra("main", this);
        Intent serviceNavi = new Intent(getApplicationContext(), OverlayShowingButtonServiceNavi.class);
        startService(serviceNavi);
        serviceNavi.putExtra("main", this);
//        startService(new Intent(getApplicationContext(), OverlayShowingButtonServiceKi.class));

//        getTimeAndPos();
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

    private void setFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
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
}
