package com.android.kk.carleft;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.kk.carapplication.ActivityRunOnStartup;
import com.android.kk.carapplication.MainActivity;

import java.io.IOException;


public class ActivityRunOnStartupCarLeft extends ActivityRunOnStartup {

    @Override
    public void onReceive(Context context, Intent intent) {
        mainActivityClass = CarLeftActivity.class;
        super.onReceive(context, intent);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            try {
                MainActivity.cmd("su", PowerConnectionReceiver.halt + ".sh", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
