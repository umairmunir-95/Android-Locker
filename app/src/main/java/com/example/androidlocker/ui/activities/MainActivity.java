package com.example.androidlocker.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidlocker.App;
import com.example.androidlocker.R;
import com.example.androidlocker.utils.BarcodeScanner;
import com.example.androidlocker.utils.DialogManager;
import com.example.androidlocker.utils.ImageSaver;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_take_picture) Button btnTakePicture;
    @BindView(R.id.btn_view_picture) Button btnView;
    @BindView(R.id.tv_title) TextView tvTitle;
    private ImageSaver imageSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode==Activity.RESULT_OK) {
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            imageSaver.saveImage(App.FOLDER_NAME, UUID.randomUUID().toString(),bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        System.exit(0);
    }

    private void initViews()
    {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.app_name));
        imageSaver=new ImageSaver(MainActivity.this);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(getResources().getString(R.string.take_picture));
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(getResources().getString(R.string.view));

            }
        });
    }

    private void requestPermissions(final String requestType) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (requestType.equals(getResources().getString(R.string.take_picture)))
                            {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 1);
                            }
                            else
                            {
                                BarcodeScanner.scanBarCode(MainActivity.this);
                            }
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            DialogManager.showSettingsDialog(MainActivity.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}