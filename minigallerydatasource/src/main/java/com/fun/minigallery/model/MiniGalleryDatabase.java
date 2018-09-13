package com.fun.minigallery.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
@Database(entities = {GalleryInfo.class}, version = 1)
public abstract class MiniGalleryDatabase extends RoomDatabase {
    private static final Object sLock = new Object();
    private static volatile MiniGalleryDatabase instance;

    public abstract GalleryDao galleryDao();

    public static MiniGalleryDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (sLock) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), MiniGalleryDatabase.class, "gallery").build();
                }
            }
        }
        return instance;
    }
}
