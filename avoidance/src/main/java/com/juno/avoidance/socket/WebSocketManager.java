package com.juno.avoidance.socket;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import com.google.gson.Gson;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.juno.avoidance.mvp.model.entity.http.Response.*;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 15:33.
 * When I met you in the summer.
 * Description :
 */
public class WebSocketManager extends WebSocketListener {

    private Gson gson;
    private OkHttpClient client;

    private WebSocketManager(Gson gson, OkHttpClient client) {
        this.gson = gson;
        this.client = client;
    }

    public static WebSocketManager connect(Context context, String url) {
        AppComponent appComponent = ArmsUtils.obtainAppComponentFromContext(context);
        return new WebSocketManager(appComponent.gson(), appComponent.okHttpClient()).init();
    }


    private WebSocketManager init() {
        client.newWebSocket(new Request.Builder().build(), this);
        return this;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        ResponseRecord record = gson.fromJson(text, ResponseRecord.class);
        EventBus.getDefault().post(record);
    }

}
