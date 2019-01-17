package com.example.androidlocker.utils;

public class StringUtils {

    public static String getFilteredImageName(String imageName)
    {
        return imageName.substring(imageName.lastIndexOf("/") + 1);
    }
}
