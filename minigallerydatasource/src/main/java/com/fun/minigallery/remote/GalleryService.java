package com.fun.minigallery.remote;


import com.fun.minigallery.model.GalleryEntity;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
public interface GalleryService {
    @GET
    Call<List<GalleryEntity>> getGalleryInfo(@Url String url);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
