package com.juno.avoidance.mvp.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 21:00.
 * When I met you in the summer.
 * Description : 设备实体类
 */
@Data
@Builder
public class Device {

    private String url;
    private String title;
    private String description;
    private boolean state;

    public static Device getInstance() {
        return new DeviceBuilder()
                .url("123")
                .title("Kangaroo Valley Safari")
                .description("Located two hours south of Sydney in the Southern Highlands of New South Wales,...")
                .state(true)
                .build();
    }

}
