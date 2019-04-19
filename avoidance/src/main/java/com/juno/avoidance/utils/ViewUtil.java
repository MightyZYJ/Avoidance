package com.juno.avoidance.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.view.View;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 10:08.
 * When I met you in the summer.
 * Description : 作用于一般的View上
 */
public class ViewUtil {

    /**
     * Created by Juno at 20:08, 2019/4/16.
     * animate description : 使按钮按下时有Z轴位移的效果
     */
    @SuppressLint("NewApi")
    public static void animate(View v) {
        v.animate().translationZ(15f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.animate().translationZ(1.0f).setDuration(500);
                    }

                });
    }

}
