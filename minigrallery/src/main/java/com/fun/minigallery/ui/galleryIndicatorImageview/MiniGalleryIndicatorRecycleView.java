package com.fun.minigallery.ui.galleryIndicatorImageview;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.fun.minigallery.model.GalleryEntity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/17.
 */
public class MiniGalleryIndicatorRecycleView extends DiscreteScrollView {
    private IndicatorAdapter indicatorAdapter;

    public MiniGalleryIndicatorRecycleView(@NonNull Context context) {
        this(context, null);
    }

    public MiniGalleryIndicatorRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initIndicator();
    }
    private void initIndicator() {
        indicatorAdapter = new IndicatorAdapter();
        this.setSlideOnFling(true);
        this.setAdapter(indicatorAdapter);
        this.setItemTransitionTimeMillis(200);
        this.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        indicatorAdapter.notifyDataSetChanged();
    }

    public void updateData(List<GalleryEntity> galleryEntities){
        indicatorAdapter.updateData(galleryEntities);
    }

    public int getCount(){
        return indicatorAdapter.getItemCount();
    }
}
