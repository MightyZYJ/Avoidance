package com.juno.avoidance.mvp.model.entity;

import com.jess.arms.base.App;
import com.juno.avoidance.mvp.model.api.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 22:04.
 * When I met you in the summer.
 * Description :
 */
public class DeviceFactory {

    public static Device create(int i) {
        String url = Api.APP_DOMAIN + "/" + i + ".jpg";
        return new Device.DeviceBuilder()
                .url(url)
                .title("我的设备" + i)
                .build();
    }

    public static List<Device> create() {
        List<Device> devices = new ArrayList<>();

        Device device = create(1);
        device.setState(true);
        device.setTitle("智能出行辅助设备");
        device.setDescription("可穿戴式智能辅助出行设备是一种面向视力障碍人群设计的智能出行导引设备");
        devices.add(device);

        Device device1 = create(2);
        device1.setState(false);
        device1.setTitle("老人防摔倒手环");
        device1.setDescription("一种老人防跌倒报警手环,包括手环主体、防水防护壳、加速度传感器、数据处理模块、电源和报警器");
        devices.add(device1);

        Device device2 = create(3);
        device2.setState(false);
        device2.setTitle("我的扫地机器人");
        device2.setDescription("查看我的扫地机器人跑到哪去了");
        devices.add(device2);

        Device device3 = create(4);
        device3.setState(false);
        device3.setTitle("我的VLOG");
        device3.setDescription("防止摄影机丢失，已经加入定位设备");
        devices.add(device3);

        return devices;
    }

}
