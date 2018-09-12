package com.fun.minigallery.galleryrecyclellview;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fun.minigallery.model.GalleryInfo;
import com.fun.minigrallery.R;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class MiniGalleryView extends LinearLayout {
    private DiscreteScrollView rvIndicator;
    private ImageView ivBack;
    private ViewPager vpVideo;
    private InfiniteScrollAdapter infiniteAdapter;
    private IndicatorAdapter indicatorAdapter;

    private List<GalleryInfo> galleryInfoList = new ArrayList<>();
    private OnClickListener onItemClickListener;

    public MiniGalleryView(Context context) {
        this(context,null);
    }

    public MiniGalleryView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MiniGalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setOnBackClickListener(OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initView(final Context context) {
        setOrientation(VERTICAL);
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
        initIndicator();
        initVideoPageView();
    }

    private void initIndicator() {
        indicatorAdapter = new IndicatorAdapter();
        infiniteAdapter = InfiniteScrollAdapter.wrap(indicatorAdapter);
        rvIndicator.setOrientation(DSVOrientation.HORIZONTAL);
        rvIndicator.setAdapter(infiniteAdapter);
        rvIndicator.setItemTransitionTimeMillis(200);
        rvIndicator.setSlideOnFling(true);
        rvIndicator.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
    }

    private void initVideoPageView() {

    }

    public void updateData(List<GalleryInfo> galleryInfoList) {
        this.galleryInfoList.clear();
        this.galleryInfoList.addAll(galleryInfoList);
        indicatorAdapter.updateData(galleryInfoList);
    }
}
