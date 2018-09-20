package com.fun.minigallery.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.fun.minigallery.download.FileUtil;

import java.io.File;


/**
 * @author jacky_zhou
 * @version 2018/9/13.
 * 命中规则先不做处理了
 */
public class GlideUtil {

    public static void disPlayImage(Context context,String url,ImageView imageView){

        Glide.with(context).load(url).into(imageView);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        GlideUtil.disPlayImage(imageView.getContext(),url,imageView);
    }

    @BindingAdapter({"videoUrl","videoLocalPath"})
    public static void loadVideo(VideoView videoView,String videoUrl,String videoLocalPath){
        if (videoLocalPath == null || !(new File(videoLocalPath).exists())){
            FileUtil.downFile(videoUrl,videoLocalPath);
            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);
            videoView.start();
        }else {
            videoView.setVideoPath(videoLocalPath);
            videoView.start();
        }
    }
}
