package com.juno.avoidance.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.api.Api;
import com.juno.avoidance.mvp.model.entity.http.Response;
import com.juno.avoidance.mvp.ui.activity.EmergencyActivity;
import com.juno.avoidance.mvp.ui.activity.MapActivity;
import com.juno.avoidance.utils.NotificationUtil;

import org.greenrobot.eventbus.Subscribe;

import static com.juno.avoidance.utils.NotificationUtil.Manager.CHANNEL_ID;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 15:21.
 * When I met you in the summer.
 * Description : 前台服务
 */
@SuppressWarnings("unused")
public class SocketService extends Service {

    private NotificationUtil.Manager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        WebSocketManager.connect(this, Api.APP_SOCKET);
        manager = new NotificationUtil.Manager().context(this);
        startForeground(100, new NotificationCompat.Builder(this, CHANNEL_ID).setPriority(NotificationCompat.PRIORITY_MAX).build());
    }

    @Subscribe
    public void receive(Response.ResponseRecord responseRecord) {
        manager.create("收到报警信息！！", "你的关注对象发出报警信息，请速查看", R.drawable.ic_locate, EmergencyActivity.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.destroy();
    }
}
