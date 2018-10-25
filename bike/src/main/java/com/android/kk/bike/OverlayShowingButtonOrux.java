package com.android.kk.bike;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.kk.carapplication.OverlayShowingButton;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OverlayShowingButtonOrux extends OverlayShowingButton {
    public OverlayShowingButtonOrux() {
        super("orux", 660, 1010, 16, 97,"com.orux.oruxmaps");
    }

    public void setBrightness(){

        int brightness = 1;
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        long timeInMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        Log.d("kkLog", "dayOfYear: " + dayOfYear);
        Log.d("kkLog", "time: " + timeInMinutes);
        //constrain the value of brightness
        if(brightness < 0)
            brightness = 0;
        else if(brightness > 255)
            brightness = 255;

        ContentResolver cResolver = this.getApplicationContext().getContentResolver();
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);

    }

    public boolean openApp() {
        setBrightness();
        return super.openApp();
    }
}
