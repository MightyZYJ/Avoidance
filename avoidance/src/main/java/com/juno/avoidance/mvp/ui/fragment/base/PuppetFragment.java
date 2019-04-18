package com.juno.avoidance.mvp.ui.fragment.base;

import android.os.Bundle;

import com.jess.arms.mvp.IPresenter;
import com.jkb.fragment.rigger.annotation.Puppet;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 17:39.
 * When I met you in the summer.
 * Description :
 */
@Puppet
public abstract class PuppetFragment<T extends IPresenter> extends BindFragment<T> {
    public void onLazyLoadViewCreated(Bundle savedInstanceState) {
        //do something in here
    }
}
