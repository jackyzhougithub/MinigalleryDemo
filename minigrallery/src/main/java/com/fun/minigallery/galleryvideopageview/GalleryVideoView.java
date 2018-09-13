package com.fun.minigallery.galleryvideopageview;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fun.minigallery.model.GalleryInfo;
import com.fun.minigallery.util.DeviceUtil;
import com.fun.minigrallery.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * @author jacky_zhou
 * @version 2018/9/13.
 */
public class GalleryVideoView extends ViewPager {

    private List<GalleryInfo> galleryInfoList = new ArrayList<>();
    private PageAdapter pageAdapter;

    public GalleryVideoView(@NonNull Context context) {
        this(context,null);
    }

    public GalleryVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (galleryInfoList.size() == 0){
            return;
        }
        pageAdapter.relayoutVideoLayout(newConfig.orientation);
        pageAdapter.notifyDataSetChanged();
    }

    private void initView(){
        pageAdapter = new PageAdapter();
        setAdapter(pageAdapter);
    }

    public void updateData(List<GalleryInfo> galleryInfoList) {
        this.galleryInfoList.clear();
        this.galleryInfoList.addAll(galleryInfoList);
        pageAdapter.notifyDataSetChanged();
    }

    private class PageAdapter extends PagerAdapter {
        private int mChildCount = 0;
        private LoopVideoView videoView;
        private View rootView;
        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return galleryInfoList.size();
        }

        @Override
        public int getItemPosition(Object object)   {
            if ( mChildCount > 0) {
                mChildCount --;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            rootView = LayoutInflater.from(container.getContext()).inflate(R.layout.minigallery_video_adapter,container,false);
            GalleryInfo galleryInfo = galleryInfoList.get(position);
            videoView = rootView.findViewById(R.id.video);
            int oreintation = DeviceUtil.isPort(container.getContext()) ? ORIENTATION_PORTRAIT : ORIENTATION_LANDSCAPE;
            relayoutVideoLayout(oreintation);
            setVideo(galleryInfo,videoView);
            container.addView(rootView);
            return rootView;
        }

        private void relayoutVideoLayout(int orientation){
            if (videoView == null){
                return;
            }
            ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
            if (ORIENTATION_LANDSCAPE == orientation){
                layoutParams.width =layoutParams.height;
            }else {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            videoView.setLayoutParams(layoutParams);
        }

        private void setVideo(GalleryInfo galleryInfo,LoopVideoView videoView){
            if (galleryInfo.getLocalVideoPath() == null){
                Uri uri = Uri.parse(galleryInfo.getVideoUrl());
                videoView.setVideoURI(uri);
                videoView.start();
            }else {
                // use local
                videoView.setVideoPath(galleryInfo.getLocalVideoPath());
                videoView.start();
            }
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

}
