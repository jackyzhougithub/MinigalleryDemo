package com.fun.minigallery.ui.galleryIndicatorImageview;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fun.minigallery.model.GalleryEntity;
import com.fun.minigallery.util.DeviceUtil;
import com.fun.minigrallery.R;
import com.fun.minigrallery.databinding.MinigalleryIndicatorAdapterBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.ViewHolder> {
    private List<GalleryEntity> galleryInfoList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MinigalleryIndicatorAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.minigallery_indicator_adapter, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.resetLayoutByConfigChanged();
        holder.binding.setGalleryInfo(galleryInfoList.get(position));
        holder.binding.executePendingBindings();
    }

    public void updateData(List<GalleryEntity> galleryInfoList) {
        this.galleryInfoList.clear();
        this.galleryInfoList.addAll(galleryInfoList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return galleryInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private MinigalleryIndicatorAdapterBinding binding;

        private ViewHolder(MinigalleryIndicatorAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void resetLayoutByConfigChanged(){
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            boolean isPort = DeviceUtil.isPort(itemView.getContext());
            params.width = isPort ? DeviceUtil.getScreenWidth(itemView.getContext()) / 3
                    : DeviceUtil.getScreenHeight(itemView.getContext()) / 3;
            itemView.setLayoutParams(params);
        }
    }

}
