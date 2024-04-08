package com.rudderstack.unvrsl_biometric_detector_example;

import io.flutter.embedding.android.FlutterActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RDDevice extends AppCompatActivity {

    private String TAG = "RDDevice";

    public static String pidBlock = "Nothing";

    protected final int AUTHENTICATION_REQUEST = 1;

    private String getPidResponseXml()
    {
        return "<PidOptions ver=\"1.0\">" +
                "   <Opts env=\"\" fCount=\"1\" fType=\"0\" format=\"0\" iCount=\"0\" iType=\"0\" otp=\"\" wadh=\"\" pCount=\"0\" pType=\"0\" pidVer=\"2.0\" posh=\"UNKNOWN\" timeout=\"10000\"/>" +
                "</PidOptions>";
    }


    public void getPidBlock(Context context, String packageName)
    {
        Intent intent = new Intent();
        intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
//        intent2.putExtra("PID_OPTIONS", pidOption);
//        Intent intent = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
//        intent.setPackage(packageName);
        intent.putExtra("PID_OPTIONS" , getPidResponseXml());
        Log.d(TAG, "getPidBlock: ");

        if(context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent , AUTHENTICATION_REQUEST);
        }
        else {
            Log.d(TAG, "getPidBlock: context not instance");
        }

//        ActivityCompat.startActivityForResult(intent , AUTHENTICATION_REQUEST);
//        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult o) {
//                        Log.d(TAG, "onActivityResult: ");
//                        System.out.println(TAG + "yoyoy");
//                    }
//                }
//        );
//
//        activityResultLauncher.launch(intent);
    }


}
