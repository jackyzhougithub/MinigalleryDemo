package com.fun.minigallery.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.fun.minigallery.download.DownLoadManager;
import com.fun.minigallery.download.DownloadCallback;
import com.fun.minigallery.download.DownloadException;
import com.fun.minigallery.download.FileUtil;
import com.fun.minigallery.download.MD5Tool;

import java.io.File;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
@Entity(tableName = "gallery")
public class GalleryEntity implements Gallery {
    @PrimaryKey
    private long id;

    @ColumnInfo(name = "first_col")
    private String imageUrl;
    @ColumnInfo(name = "second_col")
    private String videoUrl;

    public GalleryEntity(){

    }

    public GalleryEntity(Gallery gallery){
        this.id = gallery.getId();
        this.imageUrl = gallery.getImageUrl();
        this.videoUrl = gallery.getVideoUrl();
        this.localVideoPath = gallery.getLocalVideoPath();
    }

    @Override
    public String getLocalVideoPath() {
        localVideoPath = getLocalVideoAbsolutePath();
        if (localVideoPath == null){
            return null;
        }
        return localVideoPath;
    }
    
    private String getLocalVideoAbsolutePath(){
        if (videoUrl == null){
            return null;
        }
        String[] data= videoUrl.split("/");
        String lastNameMd5 = MD5Tool.getMD5(videoUrl);
        String lastName;
        if (data.length > 0){
            lastName = lastNameMd5 + data[data.length -1];
        }else {
            lastName = videoUrl;
        }
        try {
            localVideoPath = FileUtil.getExternalStorageDirectory().getAbsolutePath() + File.separator + "minigallery"+ File.separator + lastName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVideoPath;
    }

    public void setLocalVideoPath(String localVideoPath) {
        this.localVideoPath = localVideoPath;
    }

    private transient String localVideoPath;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof GalleryEntity) {
            GalleryEntity cp = (GalleryEntity) obj;
            return this.id == cp.id
                    && theSameImageUrl(cp.imageUrl)
                    && theSameVideoUrl(cp.videoUrl);
        }
        return false;
    }

    private boolean theSameImageUrl(String imageUrl) {
        return (this.imageUrl == null && imageUrl == null)
                || (this.imageUrl != null && this.imageUrl.equals(imageUrl));
    }

    private boolean theSameVideoUrl(String videoUrl) {
        return (this.videoUrl == null && videoUrl == null)
                || (this.videoUrl != null && this.videoUrl.equals(videoUrl));
    }

    @Override
    public int hashCode() {
        if (imageUrl == null && videoUrl == null) {
            return (int) id;
        }
        if (imageUrl == null) {
            return videoUrl.hashCode();
        } else if (videoUrl == null) {
            return imageUrl.hashCode();
        }
        return imageUrl.hashCode() + videoUrl.hashCode();
    }
}
