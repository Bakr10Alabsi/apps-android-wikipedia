package org.wikipedia.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import org.wikipedia.settings.Prefs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Common methods for dealing with runtime permissions.
 */
public final class PermissionUtil {

    public static boolean isPermitted(@NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean shouldShowWritePermissionRationale(@NonNull AppCompatActivity activity) {
        return !Prefs.askedForPermissionOnce(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean hasWriteExternalStoragePermission(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestWriteStorageRuntimePermissions(Fragment fragment, int requestCode) {
        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        // once permission is granted/denied it will continue with onRequestPermissionsResult
    }

    public static void requestWriteStorageRuntimePermissions(AppCompatActivity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        // once permission is granted/denied it will continue with onRequestPermissionsResult
    }

    private PermissionUtil() { }
}
