package com.example.androidlocker.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.androidlocker.R;

public class SharingManager {

    public static void shareImage(Context context, ImageView imageView)
    {
        Drawable drawable =imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Image Description", null);
        Uri uri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        ((Activity) context).startActivityForResult((Intent.createChooser(intent, context.getResources().getString(R.string.share_image))),1);
    }

    public static void deleteAfterShare(Context context)
    {
        String[] retCol = { MediaStore.Audio.Media._ID };
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, retCol,MediaStore.Images.Media.TITLE + "='Image Description'", null, null);
        try
        {
            if (cursor.getCount() == 0)
            {
                return;
            }
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            int cnt = context.getContentResolver().delete(uri, null, null);
        }
        finally
        {
            cursor.close();
        }
    }
}