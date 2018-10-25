package com.android.kk.carapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

public class PowerOffActivity extends Activity implements Serializable {

    public static final String TAG = PowerOffActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Process haltProc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "kilépett: " + new Date());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
