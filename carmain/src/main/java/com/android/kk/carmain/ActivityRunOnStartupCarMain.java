package com.android.kk.carmain;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.kk.carapplication.ActivityRunOnStartup;
import com.android.kk.carapplication.MainActivity;

import java.io.IOException;

public class ActivityRunOnStartupCarMain extends ActivityRunOnStartup {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainActivity.TAG, "ActivityRunOnStartupCarMain");
        mainActivityClass = CarMainActivity.class;
        super.onReceive(context, intent);
//        try {
//            String cmdRet = MainActivity.mountSda();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
