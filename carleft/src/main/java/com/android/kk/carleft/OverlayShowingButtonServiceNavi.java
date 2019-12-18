package com.android.kk.carleft;

import android.view.View;

import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonServiceMulti;

/**
 * Created by kk on 2016.12.31.
 */

public class OverlayShowingButtonServiceNavi extends OverlayShowingButtonServiceMulti {

    public static final String PACKAGE_NAME_WAZE = "com.waze, waze";
    public static final String PACKAGE_NAME_PRIMO = "com.nng.igoprimoisr.javaclient, primo";
    public static final String PACKAGE_NAME_PAL = "com.nng.igo.primong.palestine, pal, iGO_Pal";
    public static final String PACKAGE_NAME_AVIC = "jp.pioneer.mbg.avicsync, avic";
    public static final String PACKAGE_NAME_NG_ISR = "com.nng.igoprimoisrael.javaclient, isr";
    public static final String PACKAGE_NAME_NG_BASAR = "com.basarsoft.igonextgen.javaclient, basar";
    public static final String PACKAGE_NAME_SYGIC = "com.sygic.aura, sygic";

    protected void createButtons() {
        buttons.add(new OverlayShowingButton(this, "pal", 0, 700, 16, 111, PACKAGE_NAME_PAL, View.VISIBLE));
        buttons.add(new OverlayShowingButton(this, "waze", 0, 630, 16, 111, PACKAGE_NAME_WAZE, View.GONE));
        buttons.add(new OverlayShowingButton(this, "sygic", 0, 560, 16, 111, PACKAGE_NAME_SYGIC, View.GONE));
    }
}
