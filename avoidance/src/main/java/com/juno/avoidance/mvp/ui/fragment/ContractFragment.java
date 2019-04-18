package com.juno.avoidance.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jess.arms.di.component.AppComponent;
import com.juno.avoidance.R;
import com.juno.avoidance.mvp.presenter.HomePresenter;
import com.juno.avoidance.mvp.ui.fragment.base.BindFragment;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 17:33.
 * When I met you in the summer.
 * Description :
 */
public class ContractFragment extends BindFragment<HomePresenter> {
    @Override
    protected int layout() {
        return R.layout.fragment_contract;
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
