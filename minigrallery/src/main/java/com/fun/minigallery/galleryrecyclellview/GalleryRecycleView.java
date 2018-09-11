package com.fun.minigallery.galleryrecyclellview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author jacky_zhou
 * @version 2018/9/11.
 * 画廊效果的recycleview
 */
public class GalleryRecycleView extends RecyclerView {
    public GalleryRecycleView(Context context) {
        this(context,null);
    }

    public GalleryRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GalleryRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init(){

    }

    @Deprecated
    @Override
    public void setLayoutManager(LayoutManager layout) {

    }
}
