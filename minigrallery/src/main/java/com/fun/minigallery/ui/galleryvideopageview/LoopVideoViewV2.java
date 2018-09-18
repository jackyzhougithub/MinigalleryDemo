package com.fun.minigallery.ui.galleryvideopageview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.IOException;

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
    private int mCurrentBufferPercentage;
    private MediaPlayer.OnErrorListener mOnErrorListener;
    private MediaPlayer.OnInfoListener mOnInfoListener;

    private Uri mUri;

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    private boolean     mCanPause;
    private boolean     mCanSeekBack;
    private boolean     mCanSeekForward;

    private int         mSeekWhenPrepared;

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
        mVideoWidth = 0;
        mVideoHeight = 0;
        setSurfaceTextureListener(mSurfaceTextureListener);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;
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

        matrix.preTranslate((getResizedWidth() - videoWidth) / 2, (getResizedHeight() - videoHeight) / 2);
        matrix.preScale(videoWidth / (float) getResizedWidth(), videoHeight / (float) getResizedHeight());
        matrix.postScale(maxScale, maxScale, getResizedWidth() / 2, getResizedHeight() / 2);

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
        if (fixedHeight == 0) {
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

                if (mVideoWidth * height < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
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

    SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureSizeChanged(final SurfaceTexture surface, final int width, final int height) {
            boolean isValidState = (mTargetState == STATE_PLAYING);
            boolean hasValidSize = (width > 0 && height > 0);
            if (mMediaPlayer != null && isValidState && hasValidSize) {
                start();
            }
        }

        @Override
        public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height) {
            mSurface = new Surface(surface);
            openVideo();
        }

        @Override
        public boolean onSurfaceTextureDestroyed(final SurfaceTexture surface) {
            if (mSurface != null) {
                mSurface.release();
                mSurface = null;
            }
            if (mMediaController != null) {
                mMediaController.hide();
            }
            release(true);
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(final SurfaceTexture surface) {
        }
    };

    /**
     * release the media player in any state
     */
    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
            AudioManager am = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
            VideoView a;
        }
    }

    private void openVideo() {
        if (mUri == null || mSurface == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);

        AudioManager am = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        try {
            mMediaPlayer = new MediaPlayer();

            if (mAudioSession != 0) {
                mMediaPlayer.setAudioSessionId(mAudioSession);
            } else {
                mAudioSession = mMediaPlayer.getAudioSessionId();
            }
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            mMediaPlayer.setDataSource(getContext().getApplicationContext(), mUri);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();

            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING;
            attachMediaController();
        } catch (IOException ex) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
        } catch (IllegalArgumentException ex) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
        }
    }
    private void attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController.setMediaPlayer(this);
            View anchorView = this.getParent() instanceof View ?
                    (View)this.getParent() : this;
            mMediaController.setAnchorView(anchorView);
            mMediaController.setEnabled(isInPlaybackState());
        }
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
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

    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mCurrentState = STATE_PREPARED;

            mCanPause = mCanSeekBack = mCanSeekForward = true;

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
            if (mMediaController != null) {
                mMediaController.setEnabled(true);
            }
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
                getSurfaceTexture().setDefaultBufferSize(mVideoWidth, mVideoHeight);
                // We won't get a "surface changed" callback if the surface is already the right size, so
                // start the video here instead of in the callback.
                if (mTargetState == STATE_PLAYING) {
                    start();
                    if (mMediaController != null) {
                        mMediaController.show();
                    }
                } else if (!isPlaying() &&
                        (seekToPosition != 0 || getCurrentPosition() > 0)) {
                    if (mMediaController != null) {
                        // Show the media controls when we're paused into a video and make 'em stick.
                        mMediaController.show(0);
                    }
                }
            } else {
                // We don't know the video size yet, but should start anyway.
                // The video size might be reported to us later.
                if (mTargetState == STATE_PLAYING) {
                    start();
                }
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mCurrentState = STATE_PLAYBACK_COMPLETED;
                    mTargetState = STATE_PLAYBACK_COMPLETED;
                    if (mMediaController != null) {
                        mMediaController.hide();
                    }
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mMediaPlayer);
                    }
                }
            };

    private MediaPlayer.OnInfoListener mInfoListener =
            new MediaPlayer.OnInfoListener() {
                public boolean onInfo(MediaPlayer mp, int arg1, int arg2) {
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(mp, arg1, arg2);
                    }
                    return true;
                }
            };

    private MediaPlayer.OnErrorListener mErrorListener =
            new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                    mCurrentState = STATE_ERROR;
                    mTargetState = STATE_ERROR;
                    if (mMediaController != null) {
                        mMediaController.hide();
                    }

                    /* If an error handler has been supplied, use it and finish. */
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                            return true;
                        }
                    }

                    /* Otherwise, pop up an error dialog so the user knows that
                     * something bad has happened. Only try and pop up the dialog
                     * if we're attached to a window. When we're going away and no
                     * longer have a window, don't bother showing the user an error.
                     */
                    if (getWindowToken() != null) {
                        Resources r = getContext().getResources();
                        int messageId;

                        if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
                            messageId = android.R.string.VideoView_error_text_invalid_progressive_playback;
                        } else {
                            messageId = android.R.string.VideoView_error_text_unknown;
                        }

                        new AlertDialog.Builder(getContext())
                                .setMessage(messageId)
                                .setPositiveButton(android.R.string.VideoView_error_button,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                /* If we get here, there is no onError listener, so
                                                 * at least inform them that the video is over.
                                                 */
                                                if (mOnCompletionListener != null) {
                                                    mOnCompletionListener.onCompletion(mMediaPlayer);
                                                }
                                            }
                                        })
                                .setCancelable(false)
                                .show();
                    }
                    return true;
                }
            };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new MediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    mCurrentBufferPercentage = percent;
                }
            };

}
