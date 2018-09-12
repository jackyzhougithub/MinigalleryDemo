package com.fun.minigallery.download;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class DownloadPresenter {
    private static final String TAG = DownloadPresenter.class.getSimpleName();
    private static final String PREF_KEY_DOWNLOAD_OFFSET = "download.offset.";
    private static final String PREF_KEY_DOWNLOAD_TOTAL_LENGTH = "download.totalLength.";

    private DownloadCallback mCallback;
    private String mTaskId;
    private  DownloaderService mService ;
    private Call<ResponseBody> mCall;
    private boolean paused = false;
    private ProgressResponseBody mProgressResponseBody;

    private DownloaderService getDownloadService() {
        mService = generateRetrofit().create(DownloaderService.class);

        return mService;
    }

    DownloadPresenter(String taskId, DownloadCallback downloadCallback) {
        mTaskId = taskId;
        mCallback = downloadCallback;
    }

    private Retrofit generateRetrofit() {
        final String baseUrl = "http://private-04a55-videoplayer1.apiary-mock.com";
        OkHttpClient.Builder builder = generateBuilder();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
    }

    private OkHttpClient.Builder generateBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                mProgressResponseBody = new ProgressResponseBody(originalResponse.body(), mTaskId, mCallback);
                return originalResponse.newBuilder().body(mProgressResponseBody
                )
                        .build();

            }
        });
        return builder;
    }

    private void onDownloadComplete() {
        if (mCallback != null) {
            mCallback.onComplete(mTaskId);
        }
    }

    private void onDownloadCancel(final DownloadException ex) {
        if (mCallback != null) {
            mCallback.onCancel(mTaskId, ex);
        }
    }

    private void onDownloadPause() {
        if (mCallback != null) {
            mCallback.onPause(mTaskId);
        }
    }

    public void start(final String url, final String dstTmpFileName) {
        File file = null;
        long byteOffset = 0;
        try {
            file = new File(dstTmpFileName);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file == null) {
            return;
        }
        final File dstTmpFile = file;
        final boolean append = byteOffset > 0;
        final long tempByteOffset = byteOffset;
        final String sRange = String.format(Locale.CHINA, "bytes=%d-", byteOffset);
        mCall = getDownloadService().downloadFileWithDynamicUrlSync(sRange, url);
        if (mCallback != null) {
            mCallback.onStart(mTaskId);
        }
        mCall.enqueue(new Callback<ResponseBody>() {

            private void writeToFile(ResponseBody body, final File dstTmpFile) {
                BufferedSource bufferedSource = null;
                BufferedSink bufferedSink = null;
                Buffer buffer = new Buffer();
                DownloadException downloadException = null;
                long currByteOffset = tempByteOffset;
                long contentLength = body.contentLength() + currByteOffset;
                mProgressResponseBody.setStartByteOffset(currByteOffset);
                try {
                    bufferedSource = body.source();
                    bufferedSink = Okio.buffer(append ? Okio.appendingSink(dstTmpFile) : Okio.sink(dstTmpFile));
                    while (!bufferedSource.exhausted() && !paused) {
                        long count = bufferedSource.read(buffer, 4096);
                        bufferedSink.write(buffer, count);
                        currByteOffset += count;
                    }
                } catch (Throwable throwable) {
                    downloadException = new DownloadException(throwable.getMessage(), throwable);
                } finally {
                    if (bufferedSource != null) {
                        try {
                            bufferedSource.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bufferedSink != null) {
                        try {
                            bufferedSink.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    if (dstTmpFile.length() == contentLength) {
                        onDownloadComplete();
                        return;
                    }

                    if (paused) {
                        onDownloadPause();
                        return;
                    }
                    onDownloadCancel(downloadException);

                }
            }

            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    onDownloadCancel(new DownloadException(response.message(), null));
                    return;
                }

                writeToFile(response.body(), dstTmpFile);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onDownloadCancel(new DownloadException(t.getMessage(), t));
            }
        });

    }

    public void download(String url, final String tempFile) {
        start(url, tempFile);
    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    public synchronized void pause() {
        paused = true;
    }
}
