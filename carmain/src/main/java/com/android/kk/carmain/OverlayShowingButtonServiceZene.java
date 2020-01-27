package com.android.kk.carmain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.kk.carapplication.CmdRet;
import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonServiceMulti;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static com.android.kk.carmain.OverlayShowingButtonServiceKi.IGOSAVERESET;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonServiceZene extends OverlayShowingButtonServiceMulti {
    public static final String PACKAGE_NAME_GMMP = "gonemad.gmmp";
    public static final String PACKAGE_NAME_RADIO = "net.programmierecke.radiodroid2";
    public static final String PACKAGE_NAME_CONCERZENDER = "com.concertzender.radio_app";

    protected void createButtons() {
        buttons.add(new OverlayShowingButton(this, "cz", 750, 310, PACKAGE_NAME_CONCERZENDER, View.GONE));
        buttons.add(new OverlayShowingButton(this, "radio", 750, 390, PACKAGE_NAME_RADIO, View.GONE));
        buttons.add(new OverlayShowingButton(this, "zene", 750, 470, PACKAGE_NAME_GMMP, View.VISIBLE));
    }

    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        boolean ret = super.openApp(overlayShowingButton);
        if (ret) {
            try {
//                cmdRet = MainActivity.mountSda();  // /dev/block/sda csatolása
//                Toast.makeText(this, MainActivity.MOUNT_SDA + " return: " + cmdRet, Toast.LENGTH_SHORT).show();

                for (OverlayShowingButton button: buttons) {
                    if (button != overlayShowingButton && overlayShowingButton.isKillable() && button.isKillable()) {
                            {  // csak a zenét leállítani még jobb lenne
                                final CmdRet cmdRet = new CmdRet();
                                MainActivity.cmd("su", "/data/misc/user/bin/killall.sh", cmdRet,false);
                                Log.d(MainActivity.TAG, "process killall exitValue: " + button.getAppPackageName() + " " + cmdRet);
                            }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
