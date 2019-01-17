package com.example.androidlocker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.example.androidlocker.App;
import com.example.androidlocker.R;
import com.example.androidlocker.ui.activities.GalleryActivity;
import com.google.android.gms.vision.barcode.Barcode;

public class BarcodeScanner {

    public static void scanBarCode(final Context context)
    {
        MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity((Activity) context)
                .withEnableAutoFocus(true)
                .withBleepEnabled(false)
                .withBackfacingCamera()
                .withText(context.getResources().getString(R.string.scanning))
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {

                        if(validateBarcode(barcode.displayValue,App.DEFAULT_BARCODE_VALUE))
                        {
                            onSuccess(context);
                        }
                        else{
                            onFailure(context);
                        }
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    private static boolean validateBarcode(String scannedValue,String defaultValue)
    {
        if(scannedValue.contains(defaultValue))
            return true;
        else
            return false;
    }

    private static void onSuccess(Context context) {
        Intent i = new Intent(context, GalleryActivity.class);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        context.startActivity(i);
    }

    private static void onFailure(Context context)
    {
        Toast.makeText(context.getApplicationContext(),context.getResources().getString(R.string.invalid_barcode),Toast.LENGTH_LONG).show();
    }
}
