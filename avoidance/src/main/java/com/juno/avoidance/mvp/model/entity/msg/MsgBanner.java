package com.juno.avoidance.mvp.model.entity.msg;

import com.juno.avoidance.mvp.model.api.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juno.
 * Date : 2019/4/18.
 * Time : 22:30.
 * When I met you in the summer.
 * Description :
 */
public class MsgBanner {

    public List<String> urls;

    public MsgBanner(List<String> urls) {
        this.urls = urls;
    }

    public static MsgBanner getInstance() {
        List<String> urls = new ArrayList<>();
//        urls.add(Api.APP_IMAGE + "/1.jpg");
//        urls.add(Api.APP_IMAGE + "/2.jpg");
//        urls.add(Api.APP_IMAGE + "/3.jpg");
//        urls.add(Api.APP_IMAGE + "/4.jpg");
        urls.add(Api.APP_IMAGE + "/1.png");
        urls.add(Api.APP_IMAGE + "/2.png");
//        urls.add(Api.APP_IMAGE + "/3.jpg");
        return new MsgBanner(urls);
    }
}
