package com.fun.minigallery.ui.galleryvideopageview;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.fun.minigallery.util.DeviceUtil;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
public class LoopVideoView extends VideoView implements MediaPlayer.OnCompletionListener {
    private boolean enableLoop = true;
    private int videoWidth;
    private int videoHeight;
    private int layoutWidth;

    public LoopVideoView(Context context) {
        this(context, null);
    }

    public LoopVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void start() {
        super.start();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
    }

    public void setVideoSizeChanged(final MediaPlayer.OnVideoSizeChangedListener videoSizeChanged) {
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        videoWidth = mp.getVideoWidth();
                        videoHeight = mp.getVideoHeight();
                        if (videoSizeChanged != null){
                            videoSizeChanged.onVideoSizeChanged(mp, width, height);
                        }
                        relayoutVideoLayout();
                        invalidate();
                    }
                });

            }
        });
    }

    private void relayoutVideoLayout() {
        if (this.videoWidth == 0 || this.videoHeight == 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width =layoutWidth <= 0 ? DeviceUtil.getScreenWidth(getContext()) : layoutWidth;
        float ratio = (this.videoWidth * 1.0F) / this.videoHeight;
        layoutParams.height = (int) (layoutParams.width / ratio);
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(this.videoWidth, widthMeasureSpec);
        int height = getDefaultSize(this.videoHeight, heightMeasureSpec);
        if (width == 0 || height == 0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        setMeasuredDimension(width, height);
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnCompletionListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnCompletionListener(null);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!enableLoop) {
            return;
        }
        seekTo(0);
        start();
    }

    public void setLayoutWidth(int width){
        layoutWidth = width;
    }
}
