package com.example.androidlocker.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidlocker.App;
import com.example.androidlocker.R;
import com.example.androidlocker.utils.ImageSaver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewImageActivity extends AppCompatActivity {

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.toolbar_iv_back) ImageView ivBack;
    @BindView(R.id.iv_image) ImageView ivPreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        ButterKnife.bind(this);
        initViews();
        loadImage();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PreviewImageActivity.this, GalleryActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initViews() {
        tvTitle.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.preview_image));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PreviewImageActivity.this, GalleryActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void loadImage()
    {
        try
        {
            Bitmap bitmap = new ImageSaver(PreviewImageActivity.this).loadImage(App.FOLDER_NAME, getIntent().getStringExtra(getResources().getString(R.string.image_name)));
            ivPreview.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d("Exception : ",e.getMessage());
        }
    }
}
