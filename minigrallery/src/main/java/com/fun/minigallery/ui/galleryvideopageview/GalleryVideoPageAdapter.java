package com.fun.minigallery.ui.galleryvideopageview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fun.minigallery.model.GalleryEntity;
import com.fun.minigallery.util.DeviceUtil;
import com.fun.minigrallery.R;
import com.fun.minigrallery.databinding.MinigalleryVideoAdapterBinding;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * @author jacky_zhou
 * @version 2018/9/18.
 */
public class GalleryVideoPageAdapter extends PagerAdapter {
    private List<GalleryEntity> galleryInfoList = new ArrayList<>();
    private int mChildCount = 0;
    private LoopVideoView videoView;
    private View rootView;
    private LayoutInflater inflater;

    public GalleryVideoPageAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }
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
        MinigalleryVideoAdapterBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.minigallery_video_adapter, container, true);
        rootView = binding.getRoot();
        videoView = rootView.findViewById(R.id.video);
        binding.setGalleryInfo(galleryInfoList.get(position));
//        int orientation = DeviceUtil.isPort(container.getContext()) ? ORIENTATION_PORTRAIT : ORIENTATION_LANDSCAPE;
//        relayoutVideoLayout(orientation);
        return rootView;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    void updateData(List<GalleryEntity> galleryInfoList) {
        this.galleryInfoList.clear();
        this.galleryInfoList.addAll(galleryInfoList);
        notifyDataSetChanged();
    }
}