package com.juno.avoidance.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.component.AppComponent;
import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.factory.HelperFactory;
import com.juno.avoidance.mvp.model.entity.factory.RecordFactory;
import com.juno.avoidance.mvp.model.entity.msg.MsgBanner;
import com.juno.avoidance.mvp.presenter.HomePresenter;
import com.juno.avoidance.mvp.ui.adapter.HelperAdapter;
import com.juno.avoidance.mvp.ui.adapter.HistoryAdapter;
import com.juno.avoidance.mvp.ui.fragment.base.BindFragment;
import com.juno.avoidance.utils.BannerUtil;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 17:34.
 * When I met you in the summer.
 * Description :
 */
public class HelpFragment extends BindFragment<HomePresenter> {

    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.rv_helpers)
    RecyclerView helpersRv;

    @BindView(R.id.rv_help_history)
    RecyclerView historyRv;

    @Override
    protected int layout() {
        return R.layout.fragment_help;
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
        banner.setImageLoader(new BannerUtil.GlideImageLoader()).setOnBannerListener(this::position);
        banner();

        helpersRv.setLayoutManager(new LinearLayoutManager(getContext()));
        helpersRv.setAdapter(new HelperAdapter(getContext(), HelperFactory.create()));

        historyRv.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRv.setAdapter(new HistoryAdapter(getContext(), RecordFactory.create()));

    }

    /**
     * Created by Juno at 22:55, 2019/4/18.
     * banner description : 需要在异步线程获取广告
     */
    public void banner() {
        EventBus.getDefault().post(MsgBanner.getInstance());
        //获取广告url
    }

    private void position(int position) {
        //TODO
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Subscribe
    public void setBanner(MsgBanner msgBanner) {
        banner.setImages(msgBanner.urls).start();
    }

}
