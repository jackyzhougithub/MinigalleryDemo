package com.fun.minigallery.model.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fun.minigallery.model.GalleryEntity;

import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
@Dao
public interface GalleryDao  {
    @Query("SELECT * FROM gallery")
    List<GalleryEntity> getAll();

    @Update
    void insertAll(List<GalleryEntity> users);
}
