package com.android.kk.carmain;

import android.view.View;

import com.android.kk.carapplication.OverlayShowingButton;
import com.android.kk.carapplication.OverlayShowingButtonServiceNaviAbstract;

public class OverlayShowingButtonServiceNavi extends OverlayShowingButtonServiceNaviAbstract {

    public static final String PACKAGE_NAME_PRIMO = "com.nng.igoprimoisr.javaclient";
    public static final String PACKAGE_NAME_AVIC = "jp.pioneer.mbg.avicsync";
    public static final String PACKAGE_NAME_NG_ISR = "com.nng.igoprimoisrael.javaclient,isr";
    public static final String PACKAGE_NAME_NG_BASAR = "com.basarsoft.igonextgen.javaclient,basar";
    public static final String PACKAGE_NAME_SYGIC = "com.sygic.aura";

    protected void createButtons() {
        buttons.add(new OverlayShowingButton(this, "avic", 610, 500, PACKAGE_NAME_AVIC, View.VISIBLE));
        buttons.add(new OverlayShowingButton(this, "primo", 610, 375, PACKAGE_NAME_PRIMO, View.GONE));
        buttons.add(new OverlayShowingButton(this, "sygic", 610, 300, PACKAGE_NAME_SYGIC, View.GONE));
    }
}
