package com.juno.avoidance.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.component.AppComponent;
import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.factory.ContractFactory;
import com.juno.avoidance.mvp.presenter.HomePresenter;
import com.juno.avoidance.mvp.ui.adapter.ContractAdapter;
import com.juno.avoidance.mvp.ui.fragment.base.BindFragment;

import butterknife.BindView;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 17:33.
 * When I met you in the summer.
 * Description : 联系人Fragment
 */
public class ContractFragment extends BindFragment<HomePresenter> {

    @BindView(R.id.rv_contract)
    RecyclerView contractRv;

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
        contractRv.setLayoutManager(new LinearLayoutManager(getContext()));
        contractRv.setAdapter(new ContractAdapter(getContext(), ContractFactory.create()));//TODO 联系人点击事件
    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
