package com.juno.avoidance.mvp.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.mvp.IPresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 11:44.
 * When I met you in the summer.
 * Description : 绑定ButterKnife的Fragment基类
 */
public abstract class BindFragment<T extends IPresenter> extends BaseFragment<T> {

    @LayoutRes
    protected abstract int layout();

    protected abstract boolean bindView();

    private Unbinder unbinder;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (layout() != 0) {
            view = inflater.inflate(layout(), container, false);
            if (bindView()) {
                unbinder = ButterKnife.bind(this, view);
            }
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bindView()) {
            unbinder.unbind();
        }
    }
}
