package com.android.kk.carapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
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

    protected int bgrColor = 0x553344dd;
    protected int textColor = 0xffffff;
    protected Button button;
    protected View topLeftView;

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
//    private int visible = View.VISIBLE;

    protected WindowManager wm;
    protected String appPackageName;
    protected int appInd = 0;
    protected String name;

    private int textSize = 32;
    private int buttonWidth = 0;

    private LongClick buttonContainer = null;

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
        this.setVisible(visible);
        Log.d("kkLog", "OverlayShowingButton() " + name + " getVisibility(): " + this.button.getVisibility());
    }

    public void setVisible(int visible) {
        this.button.setVisibility(visible);
        Log.d("kkLog", "OverlayShowingButton.setVisible() " + name + ": " + this.button.getVisibility());
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

    public void onCreate(Context context, WindowManager wm) {
        this.wm = wm;
//        button = new Button(context);
        button.setText(name);
        button.setTextSize(textSize);
//        button.setTextColor(textColor);
        button.setBackgroundColor(bgrColor);
        button.setOnTouchListener(this);
        button.setOnClickListener(this);
        button.setOnLongClickListener(this);
//        this.button.setVisibility(visible);

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_SYSTEM_ALERT,
                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = originalXPos;
        params.y = originalYPos;
        if (buttonWidth > 0) {
            params.width = buttonWidth;
        }
        wm.addView(button, params);

        topLeftView = new View(context);
        LayoutParams topLeftParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_SYSTEM_ALERT,
                LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

        openApp();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
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
                if (moving) {
                    openAppOnTouch(this);
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
        return buttonContainer.onLongClick(v);
    }

    /** Open the app.
     * @return true if likely successful, false if unsuccessful
     */
    public boolean openApp() {
        return buttonContainer.openApp(this);
    }

    protected String getAppPackageName() {
        String[] appPackageNameParts = appPackageName.split(",");
        if (appPackageNameParts.length > 1) {
            button.setText(appPackageNameParts[1]);
        }
        return appPackageNameParts[0];
    }

    public static boolean openApp(Context context, OverlayShowingButton overlayedButton) {
        Log.d("kkLog", "OverlayShowingButton.openApp(context, overlayedButton) " + overlayedButton.name + " getVisibility(): " + overlayedButton.button.getVisibility());

        if (overlayedButton.button.getVisibility() == View.GONE) {
            return true;
        }
        Toast.makeText(context, "package " + overlayedButton.getAppPackageName() + " indítása", Toast.LENGTH_SHORT).show();
        PackageManager manager = context.getPackageManager();
        Intent intent = null;
        try {
            intent = manager.getLaunchIntentForPackage(overlayedButton.getAppPackageName());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);
        } catch (Exception e) {
        }
        if (intent == null) {
            Toast.makeText(context, "package " + overlayedButton.getAppPackageName() + " nem található", Toast.LENGTH_SHORT).show();
            return true;
        }
        overlayedButton.setVisible(View.GONE);
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

