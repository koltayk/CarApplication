package com.android.kk.carmain;

import android.content.Context;
import android.content.Intent;

import com.android.kk.carapplication.ActivityRunOnStartup;

public class ActivityRunOnStartupCarMain extends ActivityRunOnStartup {

    @Override
    public void onReceive(Context context, Intent intent) {
        mainActivityClass = CarMainActivity.class;
        super.onReceive(context, intent);
    }

}
