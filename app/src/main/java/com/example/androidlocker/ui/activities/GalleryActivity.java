package com.example.androidlocker.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidlocker.App;
import com.example.androidlocker.R;
import com.example.androidlocker.data.adapters.GalleryAdapter;
import com.example.androidlocker.data.models.GalleryModel;
import com.example.androidlocker.utils.GridViewItemDecoration;
import com.example.androidlocker.utils.Helpers;
import com.example.androidlocker.utils.ImageSaver;
import com.example.androidlocker.utils.SharingManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.toolbar_iv_back) ImageView ivBack;
    private GalleryAdapter adapter;
    private List<GalleryModel> imagesList;
    private ImageSaver imageSaver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        initViews();
        prepareGalleryData();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(GalleryActivity.this,MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            SharingManager.deleteAfterShare(GalleryActivity.this);
        }
    }

    private void initViews()
    {
        tvTitle.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.gallery));
        imagesList = new ArrayList<>();
        imageSaver=new ImageSaver(GalleryActivity.this);
        adapter = new GalleryAdapter(this, imagesList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridViewItemDecoration(2, Helpers.dpToPx(GalleryActivity.this,10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GalleryActivity.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void prepareGalleryData()
    {
        File fileDirectory = new File(imageSaver.getHomeDirectory(GalleryActivity.this));
        File[] dirFiles = fileDirectory.listFiles();
        try
        {
            if(dirFiles.length != 0)
            {
                for (int i = 0; i < dirFiles.length; i++)
                {
                    GalleryModel galleryModel = new GalleryModel("Image : "+(i+1),dirFiles[i].toString());
                    imagesList.add(galleryModel);
                    Log.d("Data : ",dirFiles[i].toString());
                }
            }
        } catch (Exception e) {
            Log.d("Exception : ", e.getMessage());
        }
        adapter.notifyDataSetChanged();
    }
}