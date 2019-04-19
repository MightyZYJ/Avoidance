package com.juno.avoidance.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.Device;
import com.juno.avoidance.mvp.model.entity.DeviceFactory;
import com.juno.avoidance.mvp.model.entity.msg.MsgTitle;
import com.juno.avoidance.mvp.presenter.HomePresenter;
import com.juno.avoidance.mvp.ui.activity.CustomizeActivity;
import com.juno.avoidance.mvp.ui.adapter.DeviceAdapter;
import com.juno.avoidance.mvp.ui.fragment.base.BindFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

import static com.juno.avoidance.utils.ObjectUtil.Again.*;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 11:37.
 * When I met you in the summer.
 * Description :
 */
public class DeviceFragment extends BindFragment<HomePresenter> implements MultiChoicesCircleButton.OnSelectedItemListener {

    @BindView(R.id.mccb_add)
    MultiChoicesCircleButton addMccb;

    @BindView(R.id.rv_devices)
    RecyclerView devicesRv;

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
    private DeviceAdapter adapter = null;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        from(new ArrayList<>()).cache(true) //设置3d选择器
                .next("add", new MultiChoicesCircleButton.Item(devices[0], getResources().getDrawable(R.drawable.icon_help), 30))
                .same(new MultiChoicesCircleButton.Item(devices[1], getResources().getDrawable(R.drawable.icon_gps), 90))
                .same(new MultiChoicesCircleButton.Item(devices[2], getResources().getDrawable(R.drawable.icon_brand), 150))
                .store()
                .another(addMccb)
                .receive("setButtonItems")
                .next("setOnSelectedItemListener", (MultiChoicesCircleButton.OnSelectedItemListener) this)
                .another(list) //设置设备列表
                .lazy(DeviceFactory::create)
                .get((Getter<List<Device>>) o -> list = o)
                .another(new DeviceAdapter(getContext(), list))
                .next("setOnItemClickListener", new DeviceAdapter.Listener(getContext(), list))
                .store()
                .another(devicesRv)
                .next("setLayoutManager", new LinearLayoutManager(getContext()))
                .receive("setAdapter");
    }

    /**
     * Created by Juno at 9:46, 2019/4/19.
     * onSelected description : 自定义设备选择盘的点击事件
     */
    @Override
    public void onSelected(MultiChoicesCircleButton.Item item, int index) {
        ArmsUtils.startActivity(new Intent(getContext(), CustomizeActivity.class).putExtra(MsgTitle.M, new MsgTitle(devices[index])));
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.setOnItemClickListener(null);
    }

}
