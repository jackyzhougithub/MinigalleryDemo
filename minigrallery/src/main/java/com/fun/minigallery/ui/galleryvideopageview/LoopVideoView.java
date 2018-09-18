package com.fun.minigallery.ui.galleryvideopageview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.fun.minigallery.util.DeviceUtil;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
public class LoopVideoView extends VideoView implements MediaPlayer.OnCompletionListener {
    private boolean enableLoop = true;
    private int width;
    private int height;
    private int orientation = -5;

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
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            setBackgroundColor(Color.TRANSPARENT);
                            width = mp.getVideoWidth();
                            height = mp.getVideoHeight();
                            int orientation = DeviceUtil.isPort(getContext()) ? ORIENTATION_PORTRAIT : ORIENTATION_LANDSCAPE;
                            if (LoopVideoView.this.orientation != orientation) {
                                relayoutVideoLayout(orientation);
                            }
                        }
                        return true;
                    }
                });

            }
        });
    }

    private void relayoutVideoLayout(int orientation) {
        this.orientation = orientation;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (ORIENTATION_LANDSCAPE == orientation) {
            int size = getVideoWidth() < getVideoHeight() ? getVideoWidth() : getVideoHeight();
            layoutParams.width = size;
            layoutParams.height = size;
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (orientation != ORIENTATION_LANDSCAPE){
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            return;
        }
        int width = getDefaultSize(this.width, widthMeasureSpec);
        int height = getDefaultSize(this.height, heightMeasureSpec);
        if (this.width > 0 && this.height > 0) {
            if (orientation == ORIENTATION_LANDSCAPE){
                int size = this.width > this.height ? height : width;
                setMeasuredDimension(size, size);
            }
        }
    }

    public int getVideoHeight() {
        return height;
    }

    public int getVideoWidth() {
        return width;
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

    public void setMeasure(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
