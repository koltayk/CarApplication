package com.android.kk.bike;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.android.kk.carapplication.MainActivity;

import java.io.Serializable;

public class BikeMainActivity extends MainActivity implements Serializable {

    public static MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_bike);
        requestSystemAlertPermission(BikeMainActivity.this,1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                // Do stuff here
            }
            else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
}

        Intent serviceOrux = new Intent(getApplicationContext(), OverlayShowingButtonOrux.class);
        startService(serviceOrux);
        serviceOrux.putExtra("main", this);
//        Intent serviceNavi = new Intent(getApplicationContext(), OverlayShowingButtonNavi.class);
//        startService(serviceNavi);
//        serviceNavi.putExtra("main", this);
        startService(new Intent(getApplicationContext(), OverlayShowingButtonKi.class));

        getTimeAndPos();
    }
}
