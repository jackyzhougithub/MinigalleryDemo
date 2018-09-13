package com.fun.minigallery.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * @author jacky_zhou
 * @version 2018/9/13.
 * 命中规则先不做处理了
 */
public class GlideUtil {

    public static void disPlayImage(Context context,String url,ImageView imageView){

        Glide.with(context).load(url).into(imageView);
    }

}
