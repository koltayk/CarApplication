package com.android.kk.bike;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;

import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonService;

import java.util.Calendar;

public class OverlayShowingButtonServiceOrux extends OverlayShowingButtonService {
    public OverlayShowingButtonServiceOrux() {
        super("orux", 660, 1010, 16, 97,"com.orux.oruxmaps");
    }

    public void setBrightness(){

        int brightness = 1;
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        long timeInMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        Log.d(MainActivity.TAG, "dayOfYear: " + dayOfYear);
        Log.d(MainActivity.TAG, "time: " + timeInMinutes);
        //constrain the value of brightness
        if(brightness < 0)
            brightness = 0;
        else if(brightness > 255)
            brightness = 255;

        ContentResolver cResolver = this.getApplicationContext().getContentResolver();
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);

    }

    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        setBrightness();
        return super.openApp(overlayShowingButton);
    }
}
