package com.fun.minigallery.download;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class DownLoadManager {
    private static final String TAG = DownLoadManager.class.getSimpleName();

    /**
     * 下载列表，暂时不考虑下载优先级
     */
    private Map<String, DownRunnable> mDownLoadMap;

    private static class Holder {
        private static DownLoadManager INSTANCE = new DownLoadManager();
    }

    private DownLoadManager() {
        mDownLoadMap = new HashMap<>();
    }

    public static DownLoadManager getInstance() {
        return Holder.INSTANCE;
    }

    public void pause(String url) {
        String taskId = DownloaderUtil.generateTaskId(url);
        DownRunnable downRunnable = mDownLoadMap.get(taskId);
        if (downRunnable != null) {
            downRunnable.pause();
        }
    }

    public void cancel(String url) {
        String taskId = DownloaderUtil.generateTaskId(url);
        DownRunnable downRunnable = mDownLoadMap.get(taskId);
        if (downRunnable != null) {
            downRunnable.cancel();
        }
    }

    public static File getFilePath(final String url, File dstDir) {
        if (TextUtils.isEmpty(url) || dstDir == null) {
            return null;
        }
        String taskId = DownloaderUtil.generateTaskId(url);
        String suffix = FileUtil.getFileSuffix(url);
        File file = new File(dstDir, taskId + "." + suffix);
        return file;
    }


    public synchronized String start(final String url, File dstDir, final DownloadCallback callback) {
        File file = getFilePath(url, dstDir);
        return start(url, file != null ? file.getAbsolutePath() : null, callback);
    }

    public synchronized String start(final String url, String fullFilePath, final DownloadCallback callback) {
        File file = new File(fullFilePath);
        if (file.exists()) {
            //已经下载完成
            return null;
        }
        String taskId = DownloaderUtil.generateTaskId(url);
        DownRunnable runnable = mDownLoadMap.get(taskId);
        if (runnable != null) {
            runnable.run();
            return runnable.mTaskId;
        }
        runnable = new DownRunnable(url, callback);
        runnable.setSaveFilePath(fullFilePath);
        mDownLoadMap.put(runnable.mTaskId, runnable);
        runnable.run();
        return runnable.mTaskId;
    }

    private class DownRunnable implements Runnable {
        private static final int CANCEL = 1;
        private static final int PAUSE = 2;
        private static final int START = 3;
        private static final int PROGRESS = 4;
        private static final int COMPLETED = 5;
        private String mTaskId;
        private DownloadCallback mCallback;
        private String mUrl;
        private String mSuffix;
        private String mTempFile;
        private boolean mIsDownloading;
        private DownloadPresenter mPresenter;
        private static final String TAG = "____PROGRESS____";

        private MyHandler mHandler;

        private DownRunnable(String url, DownloadCallback callback) {
            mTaskId = DownloaderUtil.generateTaskId(url);
            mUrl = url;
            mCallback = callback;
            mSuffix = "." + FileUtil.getFileSuffix(mUrl);
            mPresenter = new DownloadPresenter(mTaskId, this.callback);
            mTempFile = getLocalTmpFile();
//            mHandler = new MyHandler(this);
        }

        public void setSaveFilePath(String localFilePath) {
            if (TextUtils.isEmpty(localFilePath)) {
                return;
            }
            String suffix = "." + FileUtil.getFileSuffix(localFilePath);
            String filePath = FileUtil.getFilePathWithoutSuffix(localFilePath);
            mSuffix = suffix;
            mTempFile = filePath.concat(DownloaderUtil.SUFFIX_TMP);
        }

        @Override
        public void run() {
            if (mIsDownloading) {
                return;
            }
            mPresenter.download(mUrl, mTempFile);
        }

        private String getLocalTmpFile() {
            String localFilePath = DownloaderUtil.generateDownloadPath(mTaskId);
            if (localFilePath == null) {
                return null;
            }
            return localFilePath.concat(DownloaderUtil.SUFFIX_TMP);
        }

        private DownloadCallback callback = new DownloadCallback() {
            @Override
            public void onStart(final String taskId) {
                callbackInMainThread(START, null);
                mIsDownloading = true;
            }

            @Override
            public void onComplete(final String taskId) {
                callbackInMainThread(COMPLETED, null);
                mDownLoadMap.remove(taskId);
                if (mTempFile.endsWith(DownloaderUtil.SUFFIX_TMP)) {
                    String newFile = mTempFile.replace(DownloaderUtil.SUFFIX_TMP, mSuffix);
                    FileUtil.renameFile(new File(mTempFile), newFile);
                }
                mIsDownloading = false;
            }

            @Override
            public void onPause(final String taskId) {
                callbackInMainThread(PAUSE, null);
                mDownLoadMap.remove(taskId);
                mIsDownloading = false;
            }

            @Override
            public void onCancel(final String taskId, DownloadException downEx) {
                progressBean.setDownloadException(downEx);
                callbackInMainThread(CANCEL, progressBean);
                mDownLoadMap.remove(taskId);
                mIsDownloading = false;
            }

            @Override
            public void onProgress(final String taskId, final long soFar, final long total) {
                progressBean.setContentLength(total);
                long lastSoFar = progressBean.getBytesRead();
                long dxSoFar = soFar - lastSoFar;
                //每次超过1%才通知
                if (dxSoFar * 1.0f / total >= 0.01f) {
                    progressBean.setBytesRead(soFar);
                    callbackInMainThread(PROGRESS, progressBean);
                }
            }
        };

        private ProgressBean progressBean = new ProgressBean();

        private void callbackInMainThread(int messageType, ProgressBean progressBean) {
            handleMessage(messageType,progressBean);
        }

        private void handleMessage(int messageType, ProgressBean progressBean) {
            if (mCallback == null) {
                return;
            }
            switch (messageType) {
                case CANCEL:
                    mCallback.onCancel(mTaskId, progressBean.getDownloadException());
                    break;
                case PAUSE:
                    mCallback.onPause(mTaskId);
                    break;
                case COMPLETED:
                    mCallback.onComplete(mTaskId);
                    break;
                case START:
                    mCallback.onStart(mTaskId);
                    break;
                case PROGRESS:
                    if (progressBean != null) {
                        mCallback.onProgress(mTaskId, progressBean.getBytesRead(), progressBean.getContentLength());
                    }
                    break;
                default:
                    break;
            }
        }

        public void cancel() {
            if (mPresenter != null) {
                mPresenter.cancel();
            }
        }

        public void pause() {
            mPresenter.pause();
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<DownRunnable> mRef;
        private MyHandler(DownRunnable runnable){
            mRef = new WeakReference<DownRunnable>(runnable);
        }
        @Override
        public void handleMessage(Message msg) {
            DownRunnable runnable = mRef.get();
            if (runnable == null){
                return;
            }
            ProgressBean bean = null;
            if (msg.obj instanceof ProgressBean){
                bean = (ProgressBean) msg.obj;
            }
            runnable.handleMessage(msg.what,bean);
        }
    }
}
