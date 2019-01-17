package com.example.androidlocker.data.models;


public class GalleryModel {

    private String imageName;
    private String imagePath;

    public GalleryModel() {
    }

    public GalleryModel(String imageName, String imagePath) {
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}