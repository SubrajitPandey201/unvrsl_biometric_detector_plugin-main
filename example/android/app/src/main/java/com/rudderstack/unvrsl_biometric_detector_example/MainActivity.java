package com.rudderstack.unvrsl_biometric_detector_example;

import io.flutter.embedding.android.FlutterActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;

public class MainActivity extends FlutterActivity {

    private static String TAG = "MainActivity";

    private final static int AUTHENTICATION_REQUEST = 1;
    private static final String CHANNEL = "flutter.native/helper";
    private UsbManager usbManager;
    private static final String ACTION_USB_PERMISSION = "com.example.flutter_to_native.USB_PERMISSION";
    private static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";

    private HashMap<Integer, String> packageWithVid;

    private boolean doesPackageExists(Context context, String targetPackage){
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        packageWithVid = new HashMap<Integer, String>();
        packageWithVid.put(4450, "com.secugen.rdservice");
        packageWithVid.put(1947, "com.scl.rdservice");
        packageWithVid.put(8797, "com.scl.rdservice");
        packageWithVid.put(2694, "in.bioenable.rdservice.fp");
        packageWithVid.put(11576, "com.precision.pb510.rdservice");
        packageWithVid.put(11279, "com.mantra.rdservice");
        packageWithVid.put(10477, "co.aratek.asix_gms.rdservice");
        packageWithVid.put(3018, "com.acpl.registersdk_l1");


        // Register a BroadcastReceiver to listen for USB permission
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter);

        // Register a BroadcastReceiver to listen for USB device attachment
        IntentFilter attachedFilter = new IntentFilter(ACTION_USB_ATTACHED);
        registerReceiver(usbAttachedReceiver, attachedFilter);

        // Register a BroadcastReceiver to listen for USB device detachment
        IntentFilter detachedFilter = new IntentFilter(ACTION_USB_DETACHED);
        registerReceiver(usbDetachedReceiver, detachedFilter);

    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler(
                (call, result) -> {
                    if (call.method.equals("enumerateUsbDevices")) {
                        enumerateUsbDevices(result);
                    }
                    else if (call.method.equals("launchApp")) {
                        String packageName = call.arguments().toString();
                        boolean success = AppLauncher.launchAppFromPackageName(getApplicationContext(), packageName);
                        result.success(success);
                    }
                    else if ("installApp".equalsIgnoreCase(call.method)) {
                        String packageName = call.arguments().toString();
                        String url = "https://play.google.com/store/apps/details?id=" + packageName;
                        boolean success = AppLauncher.launchAppWithUrl(getApplicationContext(), url);
                        result.success(success);
                    }
                    else if ("captureRDDevice".equalsIgnoreCase(call.method)) {
                        Log.d(TAG, "configureFlutterEngine: capture rd device");
                        String packageName = call.arguments().toString();
                        RDDevice rdDevice = new RDDevice();
                        rdDevice.getPidBlock(this , packageName);
                        if(RDDevice.pidBlock != null) {
                            result.success(RDDevice.pidBlock);
                        }
                    }
                    else {
                        result.notImplemented();
                    }
                }
        );
    }

    private void enumerateUsbDevices(Result result) {
        List<Map<String, String>> usbDeviceDetailsList = new ArrayList<>();

        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);

        for (UsbDevice device : deviceList.values()) {
            if (usbManager.hasPermission(device)) {
                // You have permission, get device details
                Map<String, String> deviceDetails = new HashMap<>();
                deviceDetails.put("DeviceName", device.getDeviceName());
                deviceDetails.put("ProductId", String.valueOf(device.getProductId()));
                int vid = device.getVendorId();
                deviceDetails.put("VendorId", String.valueOf(vid));
                boolean deviceStatus = packageWithVid.containsKey(vid);
                deviceDetails.put("Device Status", String.valueOf(deviceStatus));

                if(deviceStatus)
                {
                    String packageName = packageWithVid.get(device.getVendorId());
                    deviceDetails.put(
                            "Package Status",
                            String.valueOf(doesPackageExists(this, packageWithVid.get(device.getVendorId())))
                    );
                    deviceDetails.put("Package Name" , packageName);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    deviceDetails.put("SerialNumber", String.valueOf(device.getSerialNumber()));
                }
                deviceDetails.put("Device ID", String.valueOf(device.getDeviceId()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    deviceDetails.put("USB Version", String.valueOf(device.getVersion()));
                }
                deviceDetails.put("device Protocol", String.valueOf(device.getDeviceProtocol()));

                // Retrieve manufacturer name (if available)
                String manufacturer = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    manufacturer = device.getManufacturerName();
                }
                if (manufacturer != null) {
                    deviceDetails.put("Manufacturer", manufacturer);
                }

                // Retrieve product name (if available)
                String productName = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    productName = device.getProductName();
                }
                if (productName != null) {
                    deviceDetails.put("ProductName", productName);
                }

                usbDeviceDetailsList.add(deviceDetails);
            } else {
                // Request USB permission
                usbManager.requestPermission(device, permissionIntent);
            }
        }

        // Send the USB device details to Flutter
        result.success(usbDeviceDetailsList);
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // Permission granted, get device details
                            enumerateUsbDevices(null); // Pass null as result
                        }
                    }
                }
            }
        }
    };

    private final BroadcastReceiver usbAttachedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_ATTACHED.equals(action)) {
                // A USB device has been attached, you can handle it here.
                enumerateUsbDevices(null); // Refresh the device list when a new USB device is attached
            }
        }
    };

    private final BroadcastReceiver usbDetachedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_DETACHED.equals(action)) {
                // A USB device has been detached, you can handle it here.
                enumerateUsbDevices(null); // Refresh the device list when a USB device is detached
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: in this");

        if(data != null)
        {
            switch (requestCode)
            {
                case AUTHENTICATION_REQUEST: {
                    if(resultCode == RESULT_OK)
                    {
                        Bundle bundle = data.getExtras();
                        if(bundle != null)
                        {
                            String pidData = bundle.getString("PID_DATA");
                            String dnc = bundle.getString("DNC" , "");
                            String dnr = bundle.getString("DNR" , "");

                            RDDevice.pidBlock = pidData;

                            Log.d(TAG, "onActivityResult: pidData" + pidData);
                            Log.d(TAG, "onActivityResult: dnc" + dnc);
                            Log.d(TAG, "onActivityResult: dnr" + dnr);
                        }
                    }
                }
            }
        }
    }
}