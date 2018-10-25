package com.android.kk.carmain;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.OverlayShowingButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonKi extends OverlayShowingButton {
    public static final String IGOSAVERESET = "/data/misc/user/bin/igosavereset.sh";

    boolean open = false;
    boolean longClick = false;

    public OverlayShowingButtonKi() {
        super("ki", 750, 275, "");
//        color = 0x33ddccff;
//        textColor = 0xddcc11;
    }

    @Override
    public boolean openApp() {
        if (open && !longClick) {
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonNavi.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonZene.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonKi.class));
            stopService(new Intent(getApplicationContext(), MainActivity.class));
            android.os.Process.killProcess(android.os.Process.myPid()); // kicsit brutál, de nem találtam jobbat
        }
        open = true;
        longClick = false;
        return true;
    }

    @Override
    public void openAppOnTouch() {}

    @Override
    public boolean onLongClick(View v) {
        longClick = true;
        try {
            Process process = Runtime.getRuntime().exec(IGOSAVERESET);
//            String inpStream = readFullyAsString(process.getInputStream(), Charset.defaultCharset().name());
//            String errStream = readFullyAsString(process.getErrorStream(), Charset.defaultCharset().name());
//            Log.d("OverlayShowingButtonKi", errStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "KI hosszan nyomva: " + IGOSAVERESET, Toast.LENGTH_SHORT).show();
        return false;
    }

public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
    return readFully(inputStream).toString(encoding);
}

public byte[] readFullyAsBytes(InputStream inputStream) throws IOException {
    return readFully(inputStream).toByteArray();
}

private ByteArrayOutputStream readFully(InputStream inputStream)  throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}