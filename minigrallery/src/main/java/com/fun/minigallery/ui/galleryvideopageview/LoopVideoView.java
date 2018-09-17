package com.fun.minigallery.ui.galleryvideopageview;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
public class LoopVideoView extends VideoView implements MediaPlayer.OnCompletionListener {
    private boolean enableLoop = true;
    private int width;
    private int height;


    public LoopVideoView(Context context) {
        this(context,null);
    }

    public LoopVideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoopVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

//    private void init(){
//        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mVideoWidth = mp.getVideoWidth();
//
//            }
//        });
//    }

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


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        // 默认高度，为了自动获取到focus
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = width;
//        if (this.width > 0 && this.height > 0) {
//            width = this.width;
//            height = this.height;
//        }
//        setMeasuredDimension(width, height);
//
//    }

}
