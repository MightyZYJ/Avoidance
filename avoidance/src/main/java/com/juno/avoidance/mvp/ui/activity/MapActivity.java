package com.juno.avoidance.mvp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps2d.MapView;
import com.juno.avoidance.R;
import com.juno.avoidance.utils.ObjectUtil.Again.*;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.r0adkll.slidr.Slidr;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        at(() -> ButterKnife.bind(this))
                .still(() -> Slidr.attach(this))
                .still(() -> QMUIStatusBarHelper.setStatusBarLightMode(this))
                .still(() -> mapView.onCreate(savedInstanceState));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
