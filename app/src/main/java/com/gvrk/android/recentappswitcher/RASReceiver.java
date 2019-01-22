package com.gvrk.android.recentappswitcher;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by G V RAVI KUMAR on 7/20/2018.
 */

public class RASReceiver extends BroadcastReceiver {
    int count = 0;
    List<String> newpkgNames = new ArrayList();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra("reason");
            if (reason != null) {
                if (reason.equals("recentapps")) {
                    count++;
                    ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
                    if (count == 2) {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
                            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
                            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos) {
                                for (ResolveInfo tempInstalled : pkgAppsList) {
                                    if (processInfo.processName.equals(tempInstalled.activityInfo.processName)) {
                                        if (!processInfo.processName.equals("com.gvrk.android.recentappswitcher")) {
                                            if (newpkgNames.size() < 2) {
                                                Log.v("<lollipop", "<lollipop");
                                                newpkgNames.add(processInfo.processName);
                                            } else {
                                                Toast.makeText(context, "could not get recent apps", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
                            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
                            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos) {
                                for (ResolveInfo tempInstalled : pkgAppsList) {
                                    if (processInfo.processName.equals(tempInstalled.activityInfo.processName)) {
                                        if (!processInfo.processName.equals("com.gvrk.android.recentappswitcher")) {
                                            if (newpkgNames.size() < 2 && !processInfo.processName.contains(":")) {
                                                Log.v("lollipop", ">lollipop");
                                                newpkgNames.add(processInfo.processName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < newpkgNames.size(); i++) {
                            Log.d("running", newpkgNames.get(i).toString());
                        }
                        if (newpkgNames.size() >= 2) {
                            try {
                                context.startActivity((context.getPackageManager().getLaunchIntentForPackage(newpkgNames.get(1).toString())));
                                Toast.makeText(context, "App Switched", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, "Your Phone has some problem in getting recent Tasks.", Toast.LENGTH_SHORT).show();
                        }
                        count = 0;
                        newpkgNames.clear();
                    }
                }
            }
        }
    }
}
