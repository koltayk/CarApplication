package com.android.kk.carleft;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.kk.carapplication.ActivityRunOnStartup;
import com.android.kk.carapplication.CmdRet;
import com.android.kk.carapplication.MainActivity;

import java.io.IOException;


public class ActivityRunOnStartupCarLeft extends ActivityRunOnStartup {

    @Override
    public void onReceive(Context context, Intent intent) {
        mainActivityClass = CarLeftActivity.class;
        super.onReceive(context, intent);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            try {
                final CmdRet cmdRet = new CmdRet();
                MainActivity.cmd("su", PowerConnectionReceiver.halt + ".sh", cmdRet, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
