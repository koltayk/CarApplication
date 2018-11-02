package com.android.kk.carapplication;

import android.app.Service;
import android.view.View;

public abstract class LongClick extends Service {
    public abstract void setButton(OverlayShowingButton overlayShowingButton);

    public abstract boolean openApp(OverlayShowingButton overlayShowingButton);

    public abstract void openAppOnTouch(OverlayShowingButton overlayShowingButton);

    public abstract boolean onLongClick(View v);
}
