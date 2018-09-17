package com.fun.minigallery.ui.galleryrecyclellview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fun.minigallery.ui.galleryIndicatorImageview.MiniGalleryIndicatorRecycleView;
import com.fun.minigallery.ui.galleryvideopageview.GalleryVideoView;
import com.fun.minigallery.model.GalleryEntity;
import com.fun.minigrallery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class MiniGalleryView extends FrameLayout {
    private MiniGalleryIndicatorRecycleView rvIndicator;
    private ImageView ivBack;
    private GalleryVideoView vpVideo;

    private List<GalleryEntity> galleryInfoList = new ArrayList<>();
    private OnClickListener onItemClickListener;

    public MiniGalleryView(Context context) {
        this(context, null);
    }

    public MiniGalleryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiniGalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setOnBackClickListener(OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initView(final Context context) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.minigallery_custom_gallery_layout, this, true);
        ivBack = view.findViewById(R.id.ivBack);
        rvIndicator = view.findViewById(R.id.rvIndicator);
        vpVideo = view.findViewById(R.id.vpVideo);

        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v);
                }
            }
        });
        subscribeUi();
    }

    private void subscribeUi(){

    }

}
