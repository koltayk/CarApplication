package com.android.kk.carapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public class OverlayShowingButton extends Service implements OnTouchListener, OnClickListener, OnLongClickListener {

    protected int bgrColor = 0x553344dd;
    protected int textColor = 0xffffff;
    protected Button overlayedButton;
    private View topLeftView;

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;

    protected WindowManager wm;
    protected String[] appPackageNames = {""};
    protected int appInd = 0;
    private String name;
    private int textSize = 32;
    private int buttonWidth = 0;

    public OverlayShowingButton() {
    }

    public OverlayShowingButton(String name, int xPos, int yPos) {
        this.name = name;
        this.originalXPos = xPos;
        this.originalYPos = yPos;
    }

    public OverlayShowingButton(String name, int xPos, int yPos, String appPackageName) {
        this(name, xPos, yPos);
        this.appPackageNames[appInd] = appPackageName;
    }

    public OverlayShowingButton(String name, int xPos, int yPos, int textSize, int buttonWidth, String appPackageName) {
        this(name, xPos, yPos);
        setButtonSize(textSize, buttonWidth);
        this.appPackageNames[appInd] = appPackageName;
    }

    public OverlayShowingButton(String name, int xPos, int yPos, String[] appPackageNames) {
        this(name, xPos, yPos);
        this.appPackageNames = appPackageNames;
    }

    public OverlayShowingButton(String name, int xPos, int yPos, int textSize, int buttonWidth, String[] appPackageNames) {
        this(name, xPos, yPos);
        setButtonSize(textSize, buttonWidth);
        this.appPackageNames = appPackageNames;
    }

    private void setButtonSize(int textSize, int buttonWidth) {
        this.textSize = textSize;
        this.buttonWidth = buttonWidth;
    }

    public Button getOverlayedButton() {
        return overlayedButton;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        this.wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        overlayedButton = new Button(this);
        overlayedButton.setText(name);
        overlayedButton.setTextSize(textSize);
//        overlayedButton.setTextColor(textColor);
        overlayedButton.setBackgroundColor(bgrColor);
        overlayedButton.setOnTouchListener(this);
        overlayedButton.setOnClickListener(this);
        overlayedButton.setOnLongClickListener(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = originalXPos;
        params.y = originalYPos;
        if (buttonWidth > 0) {
            params.width = buttonWidth;
        }
        wm.addView(overlayedButton, params);

        topLeftView = new View(this);
        WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

        openApp();
    }

    public void onDestroy() {
        super.onDestroy();
        if (overlayedButton != null) {
            wm.removeView(overlayedButton);
            wm.removeView(topLeftView);
            overlayedButton = null;
            topLeftView = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = event.getRawX();
                float y = event.getRawY();

                moving = false;

                int[] location = new int[2];
                overlayedButton.getLocationOnScreen(location);

                originalXPos = location[0];
                originalYPos = location[1];

                offsetX = originalXPos - x;
                offsetY = originalYPos - y;

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int[] topLeftLocationOnScreen = new int[2];
                topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

                System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
                System.out.println("originalY="+originalYPos);

                float x = event.getRawX();
                float y = event.getRawY();

                LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();

                int newX = (int) (offsetX + x);
                int newY = (int) (offsetY + y);

                if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                    return false;
                }

                params.x = newX - (topLeftLocationOnScreen[0]);
                params.y = newY - (topLeftLocationOnScreen[1]);

                wm.updateViewLayout(overlayedButton, params);
                moving = true;

                break;
            }
            case MotionEvent.ACTION_UP: {
                if (moving) {
                    openAppOnTouch();
                    return true;
                }

                break;
            }
            default:
                break;
        }
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            float x = event.getRawX();
//            float y = event.getRawY();
//
//            moving = false;
//
//            int[] location = new int[2];
//            overlayedButton.getLocationOnScreen(location);
//
//            originalXPos = location[0];
//            originalYPos = location[1];
//
//            offsetX = originalXPos - x;
//            offsetY = originalYPos - y;
//
//        }
//        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            int[] topLeftLocationOnScreen = new int[2];
//            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);
//
//            System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
//            System.out.println("originalY="+originalYPos);
//
//            float x = event.getRawX();
//            float y = event.getRawY();
//
//            LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();
//
//            int newX = (int) (offsetX + x);
//            int newY = (int) (offsetY + y);
//
//            if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
//                return false;
//            }
//
//            params.x = newX - (topLeftLocationOnScreen[0]);
//            params.y = newY - (topLeftLocationOnScreen[1]);
//
//            wm.updateViewLayout(overlayedButton, params);
//            moving = true;
//        }
//        else if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (moving) {
//                openApponTouch();
//                return true;
//            }
//        }
        return false;
    }

    public void openAppOnTouch() {
        openApp();
    }

    @Override
    public void onClick(View v) {
        openApp();
    }


    @Override
    public boolean onLongClick(View v) {
        appInd = (appInd + 1) % appPackageNames.length;
        Toast.makeText(this, "package " + getAppPackageName() + " hosszan nyomva", Toast.LENGTH_SHORT).show();
        return false;
    }


    /** Open the app.
     * @return true if likely successful, false if unsuccessful
     */
    public boolean openApp() {
        Toast.makeText(this, "package " + getAppPackageName() + " indítása", Toast.LENGTH_SHORT).show();
        PackageManager manager = this.getPackageManager();
        Intent intent = null;
        try {
            intent = manager.getLaunchIntentForPackage(getAppPackageName());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            this.startActivity(intent);
        } catch (Exception e) {
        }
        if (intent == null) {
            Toast.makeText(this, "package " + getAppPackageName() + " nem található", Toast.LENGTH_SHORT).show();
            return false;
        }

        setButton();
        return true;
    }

    protected String getAppPackageName() {
        String[] appPackageName = appPackageNames[appInd].split(",");
        if (appPackageName.length > 1) {
            overlayedButton.setText(appPackageName[1]);
        }
        return appPackageName[0];
    }

    private void setButton() {
        MainActivity activity = MainActivity.activity;
        if (activity == null) {
            return;
        }
        OverlayShowingButton button = activity.getButton();
        if (button != null) {
            button.getOverlayedButton().setVisibility(View.VISIBLE);
        }
        activity.setButton(this);
        if (appPackageNames.length == 1) {
            this.overlayedButton.setVisibility(View.GONE);
        }
        else {

        }
    }
}

