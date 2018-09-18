package com.fun.minigallery.ui.galleryvideopageview;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.fun.minigallery.model.GalleryEntity;
import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
public class GalleryVideoView extends ViewPager {


    private GalleryVideoPageAdapter pageAdapter;

    public GalleryVideoView(@NonNull Context context) {
        this(context,null);
    }

    public GalleryVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        pageAdapter = new GalleryVideoPageAdapter(getContext());
        setAdapter(pageAdapter);
    }

    public int getCount(){
        return pageAdapter.getCount();
    }

    public void updateData(List<GalleryEntity> galleryInfoList) {
        pageAdapter.updateData(galleryInfoList);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        pageAdapter.notifyDataSetChanged();
    }
}
