package com.fun.minigallery.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.fun.minigallery.AppExecutors;
import com.fun.minigallery.model.GalleryEntity;
import com.fun.minigallery.model.room.MiniGalleryDatabase;
import com.fun.minigallery.remote.GalleryRetrofit;
import com.fun.minigallery.remote.GalleryService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author jacky_zhou
 * @version 2018/9/17.
 */
public class GalleryRepository {
    private static volatile GalleryRepository sInstance;
    private MediatorLiveData<List<GalleryEntity>> observableGalleryList;
    private static final Object LOCK = new Object();
    private final AppExecutors appExecutors;
    private final MiniGalleryDatabase database;
    private boolean needSyncFromRemoteImmediately = true;
    private GalleryRepository(MiniGalleryDatabase database, String remoteUrl) {
        appExecutors = new AppExecutors();
        this.database = database;
        observableGalleryList = new MediatorLiveData<>();
        observableGalleryList.addSource(database.galleryDao().getAll(), new Observer<List<GalleryEntity>>() {
            @Override
            public void onChanged(@Nullable List<GalleryEntity> galleryEntities) {
                if (galleryEntities == null){
                    return;
                }
                observableGalleryList.postValue(galleryEntities);
            }
        });
        loadFromRemote(remoteUrl);
    }

    private void loadFromRemote(String remoteUrl){
        if (TextUtils.isEmpty(remoteUrl)){
            return;
        }
        GalleryService galleryService = GalleryRetrofit.getInstance().getGalleryService();
        Call<List<GalleryEntity>> call = galleryService.getGalleryInfo(remoteUrl);
        call.enqueue(new Callback<List<GalleryEntity>>() {
            @Override
            public void onResponse(Call<List<GalleryEntity>> call, Response<List<GalleryEntity>> response) {
                if (observableGalleryList == null || response.body() == null) {
                    return;
                }
                try {
                    List<GalleryEntity> galleryInfos = response.body();
                    if (needSyncFromRemoteImmediately) {
                        observableGalleryList.postValue(galleryInfos);
                    }
                    updateDatabase(galleryInfos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<GalleryEntity>> call, Throwable t) {
            }
        });
    }

    private void updateDatabase(final List<GalleryEntity> galleryEntities){
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.galleryDao().insertAll(galleryEntities);
            }
        });
    }

    public static GalleryRepository getInstance(final MiniGalleryDatabase database, final String remoteUrl) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new GalleryRepository(database, remoteUrl);
                }
            }
        }

        return sInstance;
    }

    public LiveData<List<GalleryEntity>> getGalleryList() {
        return observableGalleryList;
    }
}
