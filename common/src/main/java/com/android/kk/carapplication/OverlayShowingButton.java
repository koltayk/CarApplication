package com.android.kk.carapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
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

public class OverlayShowingButton implements OnTouchListener, OnClickListener, OnLongClickListener {

    public static final int LONGCLICKTIME = 1000;

    protected int bgrColor = 0x553344dd;
    protected int textColor = 0x77ffff00;
    protected Button button;
    protected View topLeftView;

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private long then = 0;
    private int visible = View.VISIBLE;

    protected WindowManager wm;
    protected String appPackageName;
    protected int appInd = 0;
    protected String name;

    private int textSize = 32;
    private int buttonWidth = 0;

    private LongClick buttonContainer = null;
    private boolean initialized = false;

    private boolean killable = false;   // nincs használatban

    public OverlayShowingButton(LongClick context) {
        this.buttonContainer = context;
        button = new Button(context);
    }

    public OverlayShowingButton(LongClick context, String name, int xPos, int yPos) {
        this(context);
        this.name = name;
        this.originalXPos = xPos;
        this.originalYPos = yPos;
    }

    public OverlayShowingButton(LongClick context, String name, int xPos, int yPos, String appPackageName) {
        this(context, name, xPos, yPos);
        this.appPackageName = appPackageName;
    }

    public OverlayShowingButton(LongClick context, String name, int xPos, int yPos, int textSize, int buttonWidth, String appPackageName) {
        this(context, name, xPos, yPos);
        setButtonSize(textSize, buttonWidth);
        this.appPackageName = appPackageName;
    }

    public OverlayShowingButton(LongClick context, String name, int xPos, int yPos, String appPackageName, int visible) {
        this(context, name, xPos, yPos, appPackageName);
        this.visible = visible;
        this.setVisible(visible);
        Log.d(MainActivity.TAG, "OverlayShowingButton() " + name + " getVisibility(): " + this.button.getVisibility());
    }

    public OverlayShowingButton(LongClick context, String name, int xPos, int yPos, int textSize, int buttonWidth, String appPackageName, int visible) {
        this(context, name, xPos, yPos, appPackageName, visible);
        setButtonSize(textSize, buttonWidth);
    }

    public OverlayShowingButton(LongClick context, String name, int xPos, int yPos, String appPackageName, int visible, boolean killable) {
        this(context, name, xPos, yPos, appPackageName, visible);
        this.killable = killable;
    }

    public void setVisible(int visible) {
        this.button.setVisibility(visible);
        Log.d(MainActivity.TAG, "OverlayShowingButton.setVisible() " + name + ": " + this.button.getVisibility());
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
    public void setButtonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
    }

    private void setButtonSize(int textSize, int buttonWidth) {
        this.textSize = textSize;
        this.buttonWidth = buttonWidth;
    }

    public Button getButton() {
        return button;
    }

    public boolean isKillable() {
        return killable;
    }

    public void onCreate(Context context, WindowManager wm) {
        Log.d(MainActivity.TAG, "OverlayShowingButton onCreate begin");
        this.wm = wm;
        button = new Button(context);
        button.setText(name);
        button.setTextSize(textSize);
        button.setTextColor(textColor);
        button.setBackgroundColor(bgrColor);
        button.setOnTouchListener(this);
        button.setOnClickListener(this);
        button.setOnLongClickListener(this);
        this.button.setVisibility(visible);

        LayoutParams params = getLayoutParams();

//        LayoutParams params = new LayoutParams(
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.TYPE_SYSTEM_ALERT,
//                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = originalXPos;
        params.y = originalYPos;
        if (buttonWidth > 0) {
            params.width = buttonWidth;
        }
        wm.addView(button, params);

        topLeftView = new View(context);
        LayoutParams topLeftParams = getLayoutParams();
//        LayoutParams topLeftParams = new LayoutParams(
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT,
//                LayoutParams.TYPE_SYSTEM_ALERT,
//                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

        openApp();
        initialized = true;
        Log.d(MainActivity.TAG, "OverlayShowingButton onCreate end");
    }

    @NonNull
    private LayoutParams getLayoutParams() {
        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
         else {
            type = LayoutParams.TYPE_SYSTEM_ALERT;
        }
        return new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                type,
                LayoutParams.FLAG_NOT_FOCUSABLE
                        | LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(MainActivity.TAG, "OverlayShowingButton onTouch begin");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                then = (Long) System.currentTimeMillis();
                float x = event.getRawX();
                float y = event.getRawY();

                moving = false;

                int[] location = new int[2];
                button.getLocationOnScreen(location);

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

                LayoutParams params = (LayoutParams) button.getLayoutParams();

                int newX = (int) (offsetX + x);
                int newY = (int) (offsetY + y);

                if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                    return false;
                }

                params.x = newX - (topLeftLocationOnScreen[0]);
                params.y = newY - (topLeftLocationOnScreen[1]);

                wm.updateViewLayout(button, params);
                moving = true;

                break;
            }
            case MotionEvent.ACTION_UP: {
                long diff = (Long) System.currentTimeMillis() - then;
                if(diff > LONGCLICKTIME){
                    return buttonContainer.onLongClick(v);
                }
                else {
                    openApp();
                    return true;
                }

//                break;
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
//            button.getLocationOnScreen(location);
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
//            LayoutParams params = (LayoutParams) button.getLayoutParams();
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
//            wm.updateViewLayout(button, params);
//            moving = true;
//        }
//        else if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (moving) {
//                openApponTouch();
//                return true;
//            }
//        }
        Log.d(MainActivity.TAG, "OverlayShowingButton onTouch end");
        return false;
    }

    public void openAppOnTouch(OverlayShowingButton button) {
        buttonContainer.openAppOnTouch(button);
    }

    @Override
    public void onClick(View v) {
        openApp();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
//        return buttonContainer.onLongClick(v);
    }

    /** Open the app.
     * @return true if likely successful, false if unsuccessful
     */
    public boolean openApp() {
        return buttonContainer.openApp(this);
    }

    public String getAppPackageName() {
        String[] appPackageNameParts = appPackageName.split(",");
        if (appPackageNameParts.length > 1) {
            button.setText(appPackageNameParts[1]);
        }
        return appPackageNameParts[0];
    }

    public boolean openApp(Context context) {
        Log.d(MainActivity.TAG, "OverlayShowingButton.openApp(context, this) " + this.name + " getVisibility(): " + this.button.getVisibility());

        if (this.button.getVisibility() == View.GONE) {
            return true;
        }
        Toast.makeText(context, "package " + this.getAppPackageName() + " indítása", Toast.LENGTH_SHORT).show();
        PackageManager manager = context.getPackageManager();
        Intent intent = null;
        try {
            intent = manager.getLaunchIntentForPackage(this.getAppPackageName());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);
        } catch (Exception e) {
        }
        if (intent == null) {
            Toast.makeText(context, "package " + this.getAppPackageName() + " nem található", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (initialized)
        this.setVisible(View.GONE);
        Log.d(MainActivity.TAG, "OverlayShowingButton.openApp(context, this) end");
        return false;
    }
//
//    private void setButton() {
//        MainActivity activity = MainActivity.activity;
//        if (activity == null) {
//            return;
//        }
//        OverlayShowingButton button = activity.getButtons();
//        if (button != null) {
//            button.getButtons().setVisibility(View.VISIBLE);
//        }
//        activity.setButton(this);
//        if (buttonContainer == null) {
//            this.button.setVisibility(View.GONE);
//        }
//        else {
//            buttonContainer.setButton(this);
//        }
//    }
}

