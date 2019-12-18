package com.android.kk.carleft;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import com.android.kk.carapplication.MainActivity;

import java.io.IOException;

public class PowerConnectionReceiver extends BroadcastReceiver {

    public static final String halt = "/data/misc/user/halt";
    private static Process haltNumProc = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                             status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        String msg = "status: " + status + " chargePlug: " + chargePlug + " isCharging: " + isCharging + " usbCharge: " + usbCharge + " acCharge: " + acCharge;
        Log.d(MainActivity.TAG, "Battery: " + msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        if (isCharging) {
            try {
                setHaltNum("0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            DevicePolicyManager policy = (DevicePolicyManager) CarLeftActivity.activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
            policy.lockNow();
            try {
                setHaltNum("1000");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setHaltNum(String num) throws IOException {
        haltNumProc = Runtime.getRuntime().exec(new String[]{"su", "-c", halt + "num.sh", num});
        Log.d(MainActivity.TAG, "Halt proc after: " + num);
    }
}
