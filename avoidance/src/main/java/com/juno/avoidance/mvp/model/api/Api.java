package com.juno.avoidance.mvp.model.api;

/**
 * ================================================
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 * <p>
 * Created by MVPArmsTemplate on 04/15/2019 20:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface Api {
    String APP_DOMAIN = "http://134.175.125.238:10110";
    //    String APP_DOMAIN = "http://192.168.31.62:8888";

    String APP_SOCKET = "ws://134.175.125.238:10110/ws?gid=1";
    //    String APP_SOCKET = "ws://192.168.31.62:8888/ws?gid=1";

    //    String APP_IMAGE = "https://raw.githubusercontent.com/MightyZYJ/Avoidance/master/image";
    //    String APP_IMAGE = "http://134.175.125.238:10003";
    String APP_IMAGE = "http://134.175.125.238:10110";

}
