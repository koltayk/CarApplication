package com.android.kk.bike;

import android.content.Intent;

import com.android.kk.carapplication.OverlayShowingButton;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonKi extends OverlayShowingButton {

    boolean open = false;
    boolean longClick = false;

    public OverlayShowingButtonKi() {
        super("ki", 0, 950, 16, 67, "");
//        color = 0x33ddccff;
//        textColor = 0xddcc11;
    }

    @Override
    public boolean openApp() {
        if (open && !longClick) {
//            stopService(new Intent(getApplicationContext(), OverlayShowingButtonNavi.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonOrux.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonKi.class));
            stopService(new Intent(getApplicationContext(), BikeMainActivity.class));
            android.os.Process.killProcess(android.os.Process.myPid()); // kicsit brutál, de nem találtam jobbat
        }
        open = true;
        longClick = false;
        return true;
    }

    @Override
    public void openAppOnTouch() {}
}