package com.fun.minigallery.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fun.minigallery.model.GalleryEntity;
import com.fun.minigallery.model.room.MiniGalleryDatabase;
import com.fun.minigallery.repository.GalleryRepository;

import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/17.
 */
public class MiniGalleryViewModel extends AndroidViewModel {
    private static final String TEST_URL = "pictures";
    private final MediatorLiveData<List<GalleryEntity>> observableGalleryList;
    private GalleryRepository repository;

    public MiniGalleryViewModel(@NonNull Application application) {
        super(application);
        observableGalleryList = new MediatorLiveData<>();
        observableGalleryList.setValue(null);
        repository = GalleryRepository.getInstance(MiniGalleryDatabase.getInstance(application),TEST_URL);
        LiveData<List<GalleryEntity>> galleryList = repository.getGalleryList();
        observableGalleryList.addSource(galleryList,new Observer<List<GalleryEntity>>() {
            @Override
            public void onChanged(@Nullable List<GalleryEntity> galleryEntities) {
                if (galleryEntities == null){
                    return;
                }
                observableGalleryList.postValue(galleryEntities);
            }
        });
    }

    public LiveData<List<GalleryEntity>> getGalleryList(){
        return observableGalleryList;
    }

}
