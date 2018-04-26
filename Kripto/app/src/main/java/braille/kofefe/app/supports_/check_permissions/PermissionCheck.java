package braille.kofefe.app.supports_.check_permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snow-Dell-07 on 3/27/2017.
 */

public class PermissionCheck {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static int PERMISSION_CODE = 101;
    Activity c;

    public PermissionCheck(Activity c) {
        this.c = c;
    }

    public boolean checkAll() {
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (!hasPermissions(c, PERMISSIONS)) {
            ActivityCompat.requestPermissions(c, PERMISSIONS, PERMISSION_CODE);
            return true;
        }
        return false;
    }

    public boolean checkCamera() {
        String[] PERMISSIONS = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!hasPermissions(c, PERMISSIONS)) {
            ActivityCompat.requestPermissions(c, PERMISSIONS, PERMISSION_CODE);
            return true;
        }
        return false;
    }


    public boolean checkLocation() {
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (!hasPermissions(c, PERMISSIONS)) {
            ActivityCompat.requestPermissions(c, PERMISSIONS, PERMISSION_CODE);
            return true;
        }
        return false;
    }

    public boolean checkReadContacts() {
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS};

        if (!hasPermissions(c, PERMISSIONS)) {
            ActivityCompat.requestPermissions(c, PERMISSIONS, PERMISSION_CODE);
            return true;
        }
        return false;
    }


    public boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(c,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(c,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(c,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(c,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
