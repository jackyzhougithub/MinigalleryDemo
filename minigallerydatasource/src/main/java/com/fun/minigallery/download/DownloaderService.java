package com.fun.minigallery.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public interface DownloaderService {
    /**
     * 下载文件
     *
     * @param fileUrl 文件地址
     * @param range   断点续传
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Header("Range") String range, @Url String fileUrl);
}
