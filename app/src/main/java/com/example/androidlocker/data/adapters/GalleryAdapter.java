package com.example.androidlocker.data.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.androidlocker.App;
import com.example.androidlocker.R;
import com.example.androidlocker.data.models.GalleryModel;
import com.example.androidlocker.ui.activities.PreviewImageActivity;
import com.example.androidlocker.utils.ImageSaver;
import com.example.androidlocker.utils.SharingManager;
import com.example.androidlocker.utils.StringUtils;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private Context mContext;
    private List<GalleryModel> imagesList;
    private ImageView selectedImageView;

    public GalleryAdapter(Context mContext, List<GalleryModel> imagesList) {
        this.mContext = mContext;
        this.imagesList = imagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sliding_image_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GalleryModel galleryModel = imagesList.get(position);
        holder.imageName.setText(galleryModel.getImageName());

        Bitmap bitmap = new ImageSaver(mContext).loadImage(App.FOLDER_NAME,StringUtils.getFilteredImageName(galleryModel.getImagePath()));
        holder.image.setImageBitmap(bitmap);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,PreviewImageActivity.class);
                i.putExtra(mContext.getResources().getString(R.string.image_name),StringUtils.getFilteredImageName(galleryModel.getImagePath()));
                mContext.startActivity(i);
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImageView=holder.image;
                showPopupMenu(holder.overflow);
            }
        });
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView imageName;
        public ImageView image, overflow;

        public MyViewHolder(View view) {
            super(view);
            imageName = view.findViewById(R.id.tv_title);
            image = view.findViewById(R.id.iv_image);
            overflow = view.findViewById(R.id.overflow);
        }
    }


    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_share:
                    SharingManager.shareImage(mContext,selectedImageView);
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }
}