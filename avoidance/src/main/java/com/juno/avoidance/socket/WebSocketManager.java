package com.juno.avoidance.socket;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import timber.log.Timber;

import com.google.gson.Gson;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.juno.avoidance.mvp.model.api.Api;
import com.juno.avoidance.mvp.model.entity.http.GpsResponse;
import com.juno.avoidance.mvp.model.entity.http.Response.*;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 15:33.
 * When I met you in the summer.
 * Description : socket监听器
 */
public class WebSocketManager extends WebSocketListener {

    private Gson gson;
    private OkHttpClient client;

    private WebSocketManager(Gson gson, OkHttpClient client) {
        this.gson = gson;
        this.client = client;
    }

    public static WebSocketManager connect(Context context) {
        AppComponent appComponent = ArmsUtils.obtainAppComponentFromContext(context);

        return new WebSocketManager(appComponent.gson(), appComponent.okHttpClient()).init();
    }


    private WebSocketManager init() {
        client.newWebSocket(new Request.Builder().url(Api.APP_SOCKET).build(), this);
        return this;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        try {
            GpsResponse response = gson.fromJson(text, GpsResponse.class);
            if (response != null) {
                EventBus.getDefault().post(response.getResult());
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        Timber.e("%s%s", code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        Timber.e(t);
    }
}
