package com.notepad.appdata;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class AppManager {
    private static final AppManager ourInstance = new AppManager();

    private AppManager() {
    }

    public static AppManager getInstance() {
        return ourInstance;
    }

    public boolean askUserToGrantPermission(final Activity activity, String[] permissions, int requestCode) {


        String permissionRequired = null;

        for (String permission : permissions)
            if (!isPermissionGranted(activity, permission)) {
                permissionRequired = permission;
                break;
            }

        // Check if the Permission is ALREADY GRANTED
        if (permissionRequired == null) return true;
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
        return false;
    }

    private boolean isPermissionGranted(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean askUserToGrantPermission(Activity activity, String permission, int code) {
        String[] permissionsRequired = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                permission};

        return askUserToGrantPermission(activity, permissionsRequired, code);
    }

}
