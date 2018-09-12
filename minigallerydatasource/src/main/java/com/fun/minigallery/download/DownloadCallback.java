package com.fun.minigallery.download;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public interface DownloadCallback {
    /**
     * 开始
     *
     * @param taskId 透传下载url
     */
    void onStart(String taskId);

    /**
     * 完成
     *
     * @param taskId 透传下载url
     */
    void onComplete(String taskId);

    /**
     * 暂停
     * @param taskId 任务id
     */
    void onPause(String taskId);

    /**
     * 取消
     * @param taskId
     */
    void onCancel(String taskId, DownloadException downEx);

    /**
     * 显示当前进度
     *
     * @param taskId   透传下载地址
     * @param soFar 当前的大小
     * @param total 总文件大小
     */
    void onProgress(String taskId, long soFar, long total);
}
