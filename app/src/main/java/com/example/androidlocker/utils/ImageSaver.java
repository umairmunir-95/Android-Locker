package com.example.androidlocker.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.androidlocker.App;
import com.example.androidlocker.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {
    
    private Context context;

    public ImageSaver(Context context) {
        this.context = context;
    }

    @NonNull
    private File createFile(String folderName,String fileName) {
        File directory;
        directory = context.getDir(folderName, Context.MODE_PRIVATE);
        if (!directory.exists() && !directory.mkdirs())
        {
            Log.d("ImageSaver", "Error creating directory " + directory);
        }
        return new File(directory, fileName);
    }

    private File getAlbumStorageDir(String albumName)
    {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public void saveImage(String folderName, String fileName, Bitmap bitmapImage)
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream(createFile(folderName,fileName));
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap loadImage(String folderName, String fileName) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile(folderName,fileName));
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getHomeDirectory(Context context)
    {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        return contextWrapper.getFilesDir().getPath().replace(context.getResources().getString(R.string.files),"")+context.getResources().getString(R.string.app)+ App.FOLDER_NAME+"/";
    }
}
