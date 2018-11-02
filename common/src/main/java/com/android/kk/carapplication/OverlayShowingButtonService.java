package com.android.kk.carapplication;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class OverlayShowingButtonService extends LongClick {

    protected OverlayShowingButton overlayedButton;
    protected WindowManager wm;
    protected String name;
    protected int xPos;
    protected int yPos;
    private int textSize = 32;
    private int buttonWidth = 0;
    protected String appPackageName;

    public OverlayShowingButtonService(String name, int xPos, int yPos, String appPackageName) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.appPackageName = appPackageName;
    }

    public OverlayShowingButtonService(String name, int xPos, int yPos, int textSize, int buttonWidth, String appPackageName) {
        this(name, xPos, yPos, appPackageName);
        this.textSize = textSize;
        this.buttonWidth = buttonWidth;
    }

    private void setButtonSize(int textSize, int buttonWidth) {
        this.overlayedButton.setTextSize(textSize);
        this.overlayedButton.setButtonWidth(buttonWidth);
    }

    public OverlayShowingButton getOverlayedButton() {
        return overlayedButton;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        createButton();
        this.wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        this.overlayedButton.onCreate(this, this.wm);
        MainActivity.activity.getButtons().add(this.overlayedButton);
    }

    protected void createButton() {
        this.overlayedButton = new OverlayShowingButton(this, name, xPos, yPos, appPackageName);
        setButtonSize(textSize, buttonWidth);
    }

    public void onDestroy() {
        super.onDestroy();
        if (overlayedButton != null) {
            wm.removeView(overlayedButton.button);
            wm.removeView(overlayedButton.topLeftView);
            overlayedButton = null;
            overlayedButton.topLeftView = null;
        }
    }

    @Override
    public void setButton(OverlayShowingButton overlayShowingButton) {
        this.overlayedButton = overlayShowingButton;
    }

    public void openAppOnTouch(OverlayShowingButton overlayShowingButton) {
        Log.d("kkLog", "OverlayShowingButtonService.openAppOnTouch(button) " + overlayShowingButton.name + " getVisibility(): " + overlayShowingButton.button.getVisibility());
        OverlayShowingButton.openApp(this, overlayShowingButton);
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d("kkLog", "OverlayShowingButtonService.onLongClick(): " + v);
        Toast.makeText(this, "package " + this.overlayedButton.getAppPackageName() + " hosszan nyomva", Toast.LENGTH_SHORT).show();
        OverlayShowingButton.openApp(this, this.overlayedButton);

        return false;
    }

    /** Open the app.
     * @return true if likely successful, false if unsuccessful
     * @param overlayShowingButton
     */
    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        if (OverlayShowingButton.openApp(this, this.overlayedButton)) return false;

        setButton();
        return true;
    }

    private void setButton() {
//        MainActivity activity = MainActivity.activity;
//
//        if (activity == null) {
//            return;
//        }
//        activity.getButtons().add(this.overlayedButton);
    }
}

