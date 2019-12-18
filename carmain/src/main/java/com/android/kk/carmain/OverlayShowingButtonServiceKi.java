package com.android.kk.carmain;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonServiceMulti;
import com.android.kk.carapplication.OverlayShowingButtonService;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonServiceKi extends OverlayShowingButtonService {
    public static final String IGOSAVERESET = "/data/misc/user/bin/igosavereset.sh";

    private boolean open = false;
    private boolean longClick = false;

    public OverlayShowingButtonServiceKi() {
        super("ki", 610, -30, "");
//        color = 0x33ddccff;
//        textColor = 0xddcc11;
    }

    @Override
    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        if (open && !longClick) {
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceMulti.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceZene.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceKi.class));
            stopService(new Intent(getApplicationContext(), MainActivity.class));
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid); // kicsit brutál, de nem találtam jobbat
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
            Process process = Runtime.getRuntime().exec(IGOSAVERESET);
            String inpStream = MainActivity.readFullyAsString(process.getInputStream(), Charset.defaultCharset().name());
            String errStream = MainActivity.readFullyAsString(process.getErrorStream(), Charset.defaultCharset().name());
            Log.d("OverlayButtonServiceKi", errStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "KI hosszan nyomva: " + IGOSAVERESET, Toast.LENGTH_SHORT).show();
        return false;
    }
}