package com.fun.minigallery.model;

import android.content.Context;

import com.fun.minigallery.model.remote.RemoteGalleryCache;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class MiniGalleryCache implements RemoteGalleryCache.DataCallback {

    private RemoteGalleryCache remoteGalleryCache;
    private List<GalleryInfo> galleryInfoList = new ArrayList<>();
    private String remoteUrl;
    private List<CacheChangedCallback> cacheChangedCallbacks = new ArrayList<>();
    private Context context;

    public MiniGalleryCache(Context context){
        this.context = context;
    }

    @Override
    public void syncData(List<GalleryInfo> data) {
        if (data == null) {
            return;
        }
        saveToLocal();
        sync(data);
    }

    private void sync(List<GalleryInfo> data){
        galleryInfoList.clear();
        galleryInfoList.addAll(data);
        // 根据策略刷新
        if (!needSync(data)){
            return;
        }
        notifyDataSync(data);
    }

    /**
     * 如果数据完全相同不需要刷新
     * 根据策略选择是否刷新 这里都先默认数据改变就马上同步
     * @return 是否需要刷新
     */
    private boolean needSync(List<GalleryInfo> data){
        if (galleryInfoList.size() != data.size()) {
            return true;
        }
        return theSameData(data);
    }

    private boolean theSameData(List<GalleryInfo> data ){
        if (data == null || galleryInfoList.size() != data.size()) {
            return true;
        }
        for (int i = 0; i < data.size(); i++) {
            GalleryInfo newGalleryInfo = data.get(i);
            GalleryInfo galleryInfo = galleryInfoList.get(i);
            if (galleryInfo != null && newGalleryInfo != null){
                if (!galleryInfo.equals(newGalleryInfo)){
                    return false;
                }
            }
        }

        return true;
    }

    public List<GalleryInfo> getGalleryList() {
        return galleryInfoList;
    }

    public void init(String remoteUrl) {
        remoteGalleryCache = new RemoteGalleryCache(remoteUrl);
        remoteGalleryCache.setDataCallback(this);
        this.remoteUrl = remoteUrl;
        loadFromLocal();
        syncFromRemote();
    }

    private void notifyDataSync(List<GalleryInfo> data){
        for (int i = 0; i < cacheChangedCallbacks.size(); i++) {
            CacheChangedCallback cacheChangedCallback = cacheChangedCallbacks.get(i);
            if (cacheChangedCallback == null){
                continue;
            }
            cacheChangedCallback.onSync(data);
        }
    }

    public void registerCallback(CacheChangedCallback cacheChangedCallback) {
        cacheChangedCallbacks.add(cacheChangedCallback);
    }

    public void unRegisterCallback(CacheChangedCallback cacheChangedCallback) {
        cacheChangedCallbacks.remove(cacheChangedCallback);
    }

    public void clearCallbacks(){
        cacheChangedCallbacks.clear();
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    private void syncFromRemote() {
        remoteGalleryCache.updateData(remoteUrl);
    }

    public MiniGalleryCache() {

    }

    private void loadFromLocal() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<GalleryInfo> galleryInfos = MiniGalleryDatabase.getInstance(context).galleryDao().getAll();
                if (galleryInfos != null){
                    sync(galleryInfos);
                }
            }
        });
        thread.start();
    }

    private void saveToLocal(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MiniGalleryDatabase.getInstance(context).galleryDao().insertAll(galleryInfoList);
            }
        });
        thread.start();
    }

    public interface CacheChangedCallback {
        /**
         * 数据同步
         */
        void onSync(List<GalleryInfo> data);
    }
}
