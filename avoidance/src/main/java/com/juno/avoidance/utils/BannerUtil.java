package com.juno.avoidance.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.juno.avoidance.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Juno.
 * Date : 2019/4/18.
 * Time : 22:22.
 * When I met you in the summer.
 * Description : 提供一个圆角的Banner
 */
public class BannerUtil {

    /**
     * Created by Juno at 22:23, 2019/4/18.
     * GlideImageLoader description :
     */
    public static class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }

        @Override
        public ImageView createImageView(Context context) {
            RoundedImageView roundedImageView = new RoundedImageView(context);
            roundedImageView.setPadding(40,5,40,5);
            roundedImageView.setCornerRadiusDimen(R.dimen.corner_radius_size);
            return roundedImageView;
        }
    }

}
