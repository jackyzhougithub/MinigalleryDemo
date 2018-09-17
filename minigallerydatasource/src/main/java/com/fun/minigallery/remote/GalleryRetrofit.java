package com.fun.minigallery.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
public class GalleryRetrofit {
    private Retrofit retrofit;
    private static volatile GalleryService galleryService;

    private static final class Holder {
        private static GalleryRetrofit sinstance = new GalleryRetrofit();
    }

    private GalleryRetrofit() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://private-04a55-videoplayer1.apiary-mock.com/")
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        galleryService = retrofit.create(GalleryService.class);
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder clientBuilder =
                new OkHttpClient.Builder();
        return clientBuilder.build();
    }


    public static GalleryRetrofit getInstance(){
        return Holder.sinstance;
    }

    public GalleryService getGalleryService() {
        return galleryService;
    }
}
