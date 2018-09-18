package com.fun.minigallery.ui.galleryvideopageview;

import android.content.Context;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.MediaController;

/**
 * @author jacky_zhou
 * @version 2018/9/18.
 */
public class LoopVideoViewV2 extends TextureView implements MediaController.MediaPlayerControl {
    private int fixedWidth;
    private int fixedHeight;
    private Matrix matrix;

    private Surface mSurface = null;
    private MediaPlayer mMediaPlayer = null;
    private int mAudioSession;
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaController mMediaController;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;

    public LoopVideoViewV2(Context context) {
        super(context, null);
    }

    public LoopVideoViewV2(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public LoopVideoViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideo();
    }

    private void initVideo() {

    }

    public void setFixedSize(int width, int height) {
        fixedHeight = height;
        fixedWidth = width;
        requestLayout();
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();
                    if (mVideoWidth != 0 && mVideoHeight != 0) {
                        getSurfaceTexture().setDefaultBufferSize(mVideoWidth, mVideoHeight);
                        requestLayout();
                        transformVideo(mVideoWidth, mVideoHeight);
                    }
                }
            };

    private void transformVideo(int videoWidth, int videoHeight) {
        if (getResizedHeight() == 0 || getResizedWidth() == 0) {
            return;
        }
        float sx = (float) getResizedWidth() / (float) videoWidth;
        float sy = (float) getResizedHeight() / (float) videoHeight;

        float maxScale = Math.max(sx, sy);
        if (this.matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }

        //第2步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((getResizedWidth() - videoWidth) / 2, (getResizedHeight() - videoHeight) / 2);

        //第1步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(videoWidth / (float) getResizedWidth(), videoHeight / (float) getResizedHeight());

        //第3步,等比例放大或缩小,直到视频区的一边超过View一边, 另一边与View的另一边相等. 因为超过的部分超出了View的范围,所以是不会显示的,相当于裁剪了.
        matrix.postScale(maxScale, maxScale, getResizedWidth() / 2, getResizedHeight() / 2);//后两个参数坐标是以整个View的坐标系以参考的

        setTransform(matrix);
        postInvalidate();
    }

    public int getResizedWidth() {
        if (fixedWidth == 0) {
            return getWidth();
        } else {
            return fixedWidth;
        }
    }

    public int getResizedHeight() {
        if (fixedHeight== 0) {
            return getHeight();
        } else {
            return fixedHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (fixedWidth == 0 || fixedHeight == 0) {
            defaultMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(fixedWidth, fixedHeight);
        }
    }

    protected void defaultMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = heightSpecSize;

                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    height = width * mVideoHeight / mVideoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                }
            } else {
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
