package com.fun.minigallery.ui.galleryrecyclellview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fun.minigallery.ui.galleryIndicatorImageview.MiniGalleryIndicatorRecycleView;
import com.fun.minigallery.ui.galleryvideopageview.GalleryVideoView;
import com.fun.minigallery.model.GalleryEntity;
import com.fun.minigrallery.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class MiniGalleryView extends FrameLayout {
    private MiniGalleryIndicatorRecycleView rvIndicator;
    private GalleryVideoView vpVideo;
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
        ImageView ivBack = view.findViewById(R.id.ivBack);
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
        bindLinkWork();
    }

    public void updateData(List<GalleryEntity> galleryEntities){
        vpVideo.updateData(galleryEntities);
        rvIndicator.updateData(galleryEntities);
    }

    /**
     * 由于是双向联动此处就直接选择callback
     */

    private void bindLinkWork(){
        vpVideo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (rvIndicator != null && position < rvIndicator.getCount()){
                    rvIndicator.smoothScrollToPosition(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rvIndicator.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

                if (vpVideo != null && adapterPosition < vpVideo.getCount() ){

                    vpVideo.setCurrentItem(adapterPosition,true);
                }
            }
        });
    }
}
