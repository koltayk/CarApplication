package com.android.kk.bike;

import android.content.Intent;

import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonService;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonServiceKi extends OverlayShowingButtonService {

    boolean open = false;
    boolean longClick = false;

    public OverlayShowingButtonServiceKi() {
        super("ki", 0, 950, 16, 67, "");
//        color = 0x33ddccff;
//        textColor = 0xddcc11;
    }

    @Override
    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        if (open && !longClick) {
//            stopService(new Intent(getApplicationContext(), OverlayShowingButtonNavi.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceOrux.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceKi.class));
            stopService(new Intent(getApplicationContext(), BikeMainActivity.class));
            android.os.Process.killProcess(android.os.Process.myPid()); // kicsit brutál, de nem találtam jobbat
        }
        open = true;
        longClick = false;
        return true;
    }

    @Override
    public void openAppOnTouch(OverlayShowingButton overlayShowingButton) {}
}