package com.fun.minigallery.download;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class DownloadException extends Exception {

    private String message;
    private Throwable exception;

    public DownloadException(String message, Throwable exception) {
        this.message = message;
        this.exception = exception;
    }

}
