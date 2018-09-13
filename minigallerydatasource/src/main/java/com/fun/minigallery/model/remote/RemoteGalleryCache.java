package com.fun.minigallery.model.remote;

import android.util.Log;


import com.fun.minigallery.model.GalleryInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 * remote 这部分主要针对url 动态改变 当前页面根据相应策略需要立即更新 或者是更新下次生效
 */
public class RemoteGalleryCache {
    private String fetchUrl;
    private static final String TAG = "remoteData";
    private GalleryService galleryService = GalleryRetrofit.getInstance().getGalleryService();

    private DataCallback dataCallback;

    private static Gson sGson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public RemoteGalleryCache(String fetchUrl) {
        this.fetchUrl = fetchUrl;
    }

    public void updateData(String url) {
        fetchUrl = url;
        fetchFromRemote();
    }

    public void setDataCallback(DataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }

    private void fetchFromRemote() {
        Call<List<GalleryInfo>> call = galleryService.getGalleryInfo(fetchUrl);
        call.enqueue(new Callback<List<GalleryInfo>>() {
            @Override
            public void onResponse(Call<List<GalleryInfo>> call, Response<List<GalleryInfo>> response) {
                Log.e(TAG, "onSuccess --- " + response.body());
                if (dataCallback == null || response.body() == null) {
                    return;
                }
                try {
                    List<GalleryInfo> galleryInfos = response.body();
                    dataCallback.syncData(galleryInfos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<GalleryInfo>> call, Throwable t) {
                Log.e(TAG, "onFailed --- " + t.getMessage());
            }
        });
    }

    private List<GalleryInfo> fromJson(String json) {
        Type listType = new TypeToken<List<GalleryInfo>>() {
        }.getType();
        return sGson.fromJson(json, listType);
    }

    public interface DataCallback {
        /**
         *
         */
        void syncData(List<GalleryInfo> data);
    }
}
