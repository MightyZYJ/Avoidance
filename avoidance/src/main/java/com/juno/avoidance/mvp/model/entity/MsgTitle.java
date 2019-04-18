package com.juno.avoidance.mvp.model.entity;

import java.io.Serializable;

/**
 * Created by Juno.
 * Date : 2019/4/18.
 * Time : 15:32.
 * When I met you in the summer.
 * Description : 信息类
 */
public class MsgTitle implements Serializable {
    public static final String M = "MsgTitle";
    public String title;

    public MsgTitle(String title) {
        this.title = title;
    }
}
