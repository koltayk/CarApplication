package com.android.kk.carapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kk on 2016.06.12.
 */
public class ActivityRunOnStartup extends BroadcastReceiver {

    protected Class<? extends MainActivity> mainActivityClass;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, mainActivityClass);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}
