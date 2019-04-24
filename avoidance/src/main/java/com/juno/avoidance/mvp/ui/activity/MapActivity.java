package com.juno.avoidance.mvp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.http.GpsResponse;
import com.juno.avoidance.mvp.model.entity.http.Response;
import com.juno.avoidance.utils.depend.SlidrUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.r0adkll.slidr.Slidr;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.juno.avoidance.utils.ObjectUtil.Again.at;

/**
 * Created by Juno at 21:11, 2019/4/18.
 * MapActivity description : 地图界面
 */
public class MapActivity extends AppCompatActivity {

    @BindView(R.id.map)
    MapView mapView;

    public static final LatLng HERE = new LatLng(23.035219, 113.398205);
    public static final BitmapDescriptor MARKER = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        at(() -> ButterKnife.bind(this))
                .still(() -> Slidr.attach(this, SlidrUtil.config(this)))
                .still(() -> QMUIStatusBarHelper.setStatusBarLightMode(this))
                .still(() -> mapView.onCreate(savedInstanceState))
                .another(mapView.getMap(), "animateCamera", CameraUpdateFactory.newCameraPosition(new CameraPosition(HERE, 18, 0, 0)))
                .next("addMarker", new MarkerOptions().position(HERE).title("位置").icon(MARKER))
                .clean();

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(GpsResponse.ResultBean resultBean) {
        AMap aMap = mapView.getMap();
        aMap.clear();
        LatLng latLng = new LatLng(resultBean.getLatitude(), resultBean.getLongitude());
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 0, 0)));
        aMap.addMarker(new MarkerOptions().position(latLng).title("位置").icon(MARKER));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
