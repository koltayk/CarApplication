package com.android.kk.carleft;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonService;

import java.io.IOException;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonServiceKi extends OverlayShowingButtonService {

    boolean open = false;
    boolean longClick = false;

    public OverlayShowingButtonServiceKi() {
        super("ki", 0, 800, 16, 111, "");
//        color = 0x33ddccff;
//        textColor = 0xddcc11;
    }

    @Override
    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        if (open && !longClick) {
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceNavi.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceOrux.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceKi.class));
            stopService(new Intent(getApplicationContext(), CarLeftActivity.class));
            android.os.Process.killProcess(android.os.Process.myPid()); // kicsit brutál, de nem találtam jobbat
        }
        open = true;
        longClick = false;
        return true;
    }

    @Override
    public void openAppOnTouch(OverlayShowingButton overlayShowingButton) {}

    @Override
    public boolean onLongClick(View v) {
        longClick = true;
        try {
            PowerConnectionReceiver.setHaltNum("0");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "KI hosszan nyomva: töltés figyelése megszakítva", Toast.LENGTH_SHORT).show();
        return false;
    }
}