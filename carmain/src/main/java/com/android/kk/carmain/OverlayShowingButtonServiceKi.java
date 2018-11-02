package com.android.kk.carmain;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonServiceNaviAbstract;
import com.android.kk.carapplication.OverlayShowingButtonService;

import java.io.IOException;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonServiceKi extends OverlayShowingButtonService {
    public static final String IGOSAVERESET = "/data/misc/user/bin/igosavereset.sh";

    boolean open = false;
    boolean longClick = false;

    public OverlayShowingButtonServiceKi() {
        super("ki", 750, 275, "");
//        color = 0x33ddccff;
//        textColor = 0xddcc11;
    }

    @Override
    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        if (open && !longClick) {
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceNaviAbstract.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceZene.class));
            stopService(new Intent(getApplicationContext(), OverlayShowingButtonServiceKi.class));
            stopService(new Intent(getApplicationContext(), MainActivity.class));
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
            Process process = Runtime.getRuntime().exec(IGOSAVERESET);
//            String inpStream = readFullyAsString(process.getInputStream(), Charset.defaultCharset().name());
//            String errStream = readFullyAsString(process.getErrorStream(), Charset.defaultCharset().name());
//            Log.d("OverlayShowingButtonServiceKi", errStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "KI hosszan nyomva: " + IGOSAVERESET, Toast.LENGTH_SHORT).show();
        return false;
    }
//
//public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
//    return readFully(inputStream).toString(encoding);
//}
//
//public byte[] readFullyAsBytes(InputStream inputStream) throws IOException {
//    return readFully(inputStream).toByteArray();
//}
//
//private ByteArrayOutputStream readFully(InputStream inputStream)  throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int length = 0;
//        while ((length = inputStream.read(buffer)) != -1) {
//            baos.write(buffer, 0, length);
//        }
//        return baos;
//    }
}