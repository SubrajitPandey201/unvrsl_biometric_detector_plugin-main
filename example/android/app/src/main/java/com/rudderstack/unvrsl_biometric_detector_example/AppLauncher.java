package com.rudderstack.unvrsl_biometric_detector_example;

import io.flutter.embedding.android.FlutterActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.HashMap;

public class AppLauncher {

    public static boolean launchAppFromPackageName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);

        if (intent != null) {
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true; // Successfully launched the app
        } else {
            // The app is not installed
            return false;
        }
    }

    public static String getVenderPackage(String deviceKey){
        HashMap<String,String> devicePackage = new HashMap<String, String>();
        devicePackage.put("morpho", "com.scl.rdservice");
        devicePackage.put("secugen", "com.secugen.rdservice");
        devicePackage.put("mantra", "com.mantra.rdservice");
        devicePackage.put("startek", "com.acpl.registersdk");
        devicePackage.put("startek2", "com.acpl.registersdk_l1");
        devicePackage.put("bioenable", "in.bioenable.rdservice.fp");
        devicePackage.put("aratek", "co.aratek.asix_gms.rdservice");
        devicePackage.put("precision", "com.precision.pb510.rdservice");
        return devicePackage.get(deviceKey);
    }


    public static boolean launchAppWithUrl(Context context, String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(android.net.Uri.parse(url));
        context.startActivity(intent);

        return true;
    }

}