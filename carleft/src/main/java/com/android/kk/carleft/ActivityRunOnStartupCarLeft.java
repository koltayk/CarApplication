package com.android.kk.carleft;

import android.content.Context;
import android.content.Intent;

import com.android.kk.carapplication.ActivityRunOnStartup;


public class ActivityRunOnStartupCarLeft extends ActivityRunOnStartup {

    @Override
    public void onReceive(Context context, Intent intent) {
        mainActivityClass = CarLeftActivity.class;
        super.onReceive(context, intent);
    }
}
