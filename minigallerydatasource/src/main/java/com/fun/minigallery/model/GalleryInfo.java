package com.fun.minigallery.model;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class GalleryInfo {
    private long id;
    private String imageUrl;
    private String videoUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
