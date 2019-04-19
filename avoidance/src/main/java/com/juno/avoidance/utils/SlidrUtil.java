package com.juno.avoidance.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import com.juno.avoidance.R;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 14:59.
 * When I met you in the summer.
 * Description : 提供Slidr的Config
 */
public class SlidrUtil {

    @SuppressLint("NewApi")
    public static SlidrConfig config(Context context) {
        return new SlidrConfig.Builder()
                .primaryColor(context.getColor(R.color.colorPrimary))
                .secondaryColor(context.getColor(R.color.colorPrimaryDark))
                .position(SlidrPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK).scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true)
                .edgeSize(0.18f)
                .build();
    }

}
