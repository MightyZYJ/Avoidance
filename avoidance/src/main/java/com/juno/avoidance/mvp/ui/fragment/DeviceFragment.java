package com.juno.avoidance.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.Device;
import com.juno.avoidance.mvp.model.entity.DeviceFactory;
import com.juno.avoidance.mvp.model.entity.MsgTitle;
import com.juno.avoidance.mvp.presenter.HomePresenter;
import com.juno.avoidance.mvp.ui.activity.CustomizeActivity;
import com.juno.avoidance.mvp.ui.activity.MapActivity;
import com.juno.avoidance.mvp.ui.fragment.base.BindFragment;
import com.juno.avoidance.utils.QMUIUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;

import static com.juno.avoidance.utils.ObjectUtil.Again.*;
import static com.juno.avoidance.utils.ViewUtil.animate;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 11:37.
 * When I met you in the summer.
 * Description :
 */
public class DeviceFragment extends BindFragment<HomePresenter> {

    @BindView(R.id.mccb_add)
    MultiChoicesCircleButton addMccb;

    @BindView(R.id.rv_devices)
    RecyclerView devicesRv;

    @BindString(R.string.active)
    String active;

    @BindString(R.string.inactive)
    String inactive;

    @BindArray(R.array.devices)
    String[] devices;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    protected int layout() {
        return R.layout.fragment_device;
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    private List<Device> list = null;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        from(new ArrayList<>()).cache(true)
                .next("add", new MultiChoicesCircleButton.Item(devices[0], getResources().getDrawable(R.drawable.icon_help), 30))
                .same(new MultiChoicesCircleButton.Item(devices[1], getResources().getDrawable(R.drawable.icon_gps), 90))
                .same(new MultiChoicesCircleButton.Item(devices[2], getResources().getDrawable(R.drawable.icon_brand), 150))
                .store()
                .another(addMccb)
                .receive("setButtonItems")
                .next("setOnSelectedItemListener", (MultiChoicesCircleButton.OnSelectedItemListener) (item, index)
                        -> ArmsUtils.startActivity(new Intent(getContext(), CustomizeActivity.class).putExtra(MsgTitle.M, new MsgTitle(devices[index]))))
                .another(list)
                .lazy(DeviceFactory::create)
                .get((Getter<List<Device>>) o -> list = o)
                .another(new CommonAdapter<Device>(getContext(), R.layout.item_device, list) {
                    @Override
                    protected void convert(ViewHolder holder, Device device, int position) {
                        holder.setText(R.id.tv_device_title, device.getTitle()); //标题
                        holder.setText(R.id.tv_device_description, device.getDescription()); //简介
                        Glide.with(getContext()).load(device.getUrl()).into((ImageView) holder.getView(R.id.iv_device_image)); //图片
                        ((TextView) holder.getView(R.id.tv_device_state)).setText(device.isState() ? Html.fromHtml(active) : Html.fromHtml(inactive)); //状态
                    }
                })
                .next("setOnItemClickListener", new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        at(() -> animate(view)).still(list.get(position).isState() ?
                                (Action) () -> ArmsUtils.startActivity(MapActivity.class) :
                                (Action) () -> QMUIUtil.info(getContext(), "设备未登陆").show());
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                })
                .store()
                .another(devicesRv)
                .next("setLayoutManager", new LinearLayoutManager(getContext()))
                .receive("setAdapter");
    }

    @Override
    public void setData(@Nullable Object data) {

    }

}
