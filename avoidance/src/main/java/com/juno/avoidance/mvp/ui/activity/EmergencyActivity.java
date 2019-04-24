package com.juno.avoidance.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.juno.avoidance.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 16:42.
 * When I met you in the summer.
 * Description : 紧急界面
 */
public class EmergencyActivity extends AppCompatActivity {

    @OnClick(R.id.btn_emergency_enter)
    public void enter() {
        startActivity(new Intent(this, MapActivity.class));
        finish();
    }

    @BindView(R.id.tv_emergency_location)
    TextView locationTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        ButterKnife.bind(this);
    }



}
