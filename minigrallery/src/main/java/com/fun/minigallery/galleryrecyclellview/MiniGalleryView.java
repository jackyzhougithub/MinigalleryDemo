package com.fun.minigallery.galleryrecyclellview;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fun.minigallery.galleryvideopageview.GalleryVideoView;
import com.fun.minigallery.model.GalleryInfo;
import com.fun.minigrallery.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class MiniGalleryView extends FrameLayout {
    private DiscreteScrollView rvIndicator;
    private ImageView ivBack;
    private GalleryVideoView vpVideo;
    private IndicatorAdapter indicatorAdapter;

    private List<GalleryInfo> galleryInfoList = new ArrayList<>();
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

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (galleryInfoList.size() == 0){
            return;
        }
        indicatorAdapter.notifyDataSetChanged();
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
        initIndicator();
        initVideoPageView();
    }

    private void initIndicator() {
        indicatorAdapter = new IndicatorAdapter();
        rvIndicator.setSlideOnFling(true);
        rvIndicator.setAdapter(indicatorAdapter);
        rvIndicator.setItemTransitionTimeMillis(200);
        rvIndicator.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        rvIndicator.addOnItemChangedListener(onItemChangedListener);
    }

    private DiscreteScrollView.OnItemChangedListener onItemChangedListener = new DiscreteScrollView.OnItemChangedListener() {
        @Override
        public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
            vpVideo.setCurrentItem(adapterPosition,true);
        }
    };

    private void initVideoPageView() {
        vpVideo.addOnPageChangeListener(pageChangeListener);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            rvIndicator.smoothScrollToPosition(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void updateData(List<GalleryInfo> galleryInfoList) {
        if (galleryInfoList == null || galleryInfoList.size() == 0) {
            return;
        }
        this.galleryInfoList.clear();
        this.galleryInfoList.addAll(galleryInfoList);
        indicatorAdapter.updateData(galleryInfoList);
        vpVideo.updateData(galleryInfoList);
    }
}
