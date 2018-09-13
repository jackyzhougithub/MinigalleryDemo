package com.fun.minigallery.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
@Dao
public interface GalleryDao  {
    @Query("SELECT * FROM gallery")
    List<GalleryInfo> getAll();

    @Update
    void insertAll(List<GalleryInfo> users);
}
