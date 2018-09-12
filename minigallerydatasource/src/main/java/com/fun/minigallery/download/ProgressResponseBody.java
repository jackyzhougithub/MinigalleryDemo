package com.fun.minigallery.download;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final DownloadCallback progressListener;
    private BufferedSource bufferedSource;
    private String taskId;
    private long startByteOffset;

    public ProgressResponseBody(ResponseBody responseBody,
                                String taskId,
                                DownloadCallback progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
        this.taskId = taskId;
    }

    public void setStartByteOffset(long startByteOffset) {
        this.startByteOffset = startByteOffset;
    }

    public long getStartByteOffset() {
        return startByteOffset;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }


    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            int minNotify = 0;
            private static final int MIN = 2;
            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                long totalByte = responseBody.contentLength();
                if (minNotify == MIN) {
                    minNotify = 0;
                    if (progressListener != null) {
                        progressListener.onProgress(taskId, startByteOffset + totalBytesRead, totalByte);
                    }
                }
                minNotify ++;
                return bytesRead;
            }
        };
    }
}