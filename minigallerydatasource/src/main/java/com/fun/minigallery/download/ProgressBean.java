package com.fun.minigallery.download;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class ProgressBean {
    private long bytesRead;
    private long contentLength;
    private boolean done;

    private DownloadException downloadException;

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setDownloadException(DownloadException downloadException) {
        this.downloadException = downloadException;
    }

    public DownloadException getDownloadException() {
        return downloadException;
    }
}