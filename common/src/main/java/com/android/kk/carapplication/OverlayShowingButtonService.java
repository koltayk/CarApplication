package com.android.kk.carapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    protected MainActivity activity;

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
    }

    protected void createButton() {
        this.overlayedButton = new OverlayShowingButton(this, name, xPos, yPos, appPackageName);
        setButtonSize(textSize, buttonWidth);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle extras = intent.getExtras();
//        activity = (MainActivity) extras.get("main");
        activity = MainActivity.activity;
        activity.getButtons().add(this.overlayedButton);
        Log.d(MainActivity.TAG, "OverlayShowingButtonService.activity: " + activity + " service: " + intent.hashCode());
        return flags;
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
//
//    @Override
//    public void setButton(OverlayShowingButton overlayShowingButton) {
//        this.overlayedButton = overlayShowingButton;
//    }

    public void openAppOnTouch(OverlayShowingButton overlayShowingButton) {
        overlayShowingButton.openApp(this);
        Log.d(MainActivity.TAG, "OverlayShowingButtonService.openAppOnTouch(button) end");
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(MainActivity.TAG, "OverlayShowingButtonService.onLongClick(): " + v);
        Toast.makeText(this, "package " + this.overlayedButton.getAppPackageName() + " hosszan nyomva", Toast.LENGTH_SHORT).show();
        this.overlayedButton.openApp(this);

        return false;
    }

    /** Open the app.
     * @return true if likely successful, false if unsuccessful
     * @param overlayShowingButton
     */
    public boolean openApp(OverlayShowingButton overlayShowingButton) {
        if (overlayShowingButton.openApp(this)) return false;

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

