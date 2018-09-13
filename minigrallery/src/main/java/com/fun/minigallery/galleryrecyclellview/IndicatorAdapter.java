package com.fun.minigallery.galleryrecyclellview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fun.minigallery.model.GalleryInfo;
import com.fun.minigallery.util.DeviceUtil;
import com.fun.minigallery.util.GlideUtil;
import com.fun.minigrallery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class IndicatorAdapter extends RecyclerView.Adapter {
    private List<GalleryInfo> galleryInfoList = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.minigallery_indicator_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ((ViewHolder) holder).fillData(galleryInfoList.get(position));
        }
    }

    public void updateData(List<GalleryInfo> galleryInfoList) {
        this.galleryInfoList.clear();
        this.galleryInfoList.addAll(galleryInfoList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return galleryInfoList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPic;
        private TextView tvId;

        private ViewHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams params =  itemView.getLayoutParams();
            params.width = DeviceUtil.getScreenWidth(itemView.getContext())/3;
            itemView.setLayoutParams(params);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvId = itemView.findViewById(R.id.tvId);
        }

        // todo databanding
        private void fillData(GalleryInfo galleryInfo) {
            if (galleryInfo == null) {
                return;
            }
            ViewGroup.LayoutParams params =  itemView.getLayoutParams();
            params.width = DeviceUtil.getScreenWidth(itemView.getContext())/3;
            itemView.setLayoutParams(params);
            tvId.setText(String.format(Locale.CHINA,"id = %d",galleryInfo.getId()));
            GlideUtil.disPlayImage(itemView.getContext(),galleryInfo.getImageUrl(),ivPic);

        }
    }
}
