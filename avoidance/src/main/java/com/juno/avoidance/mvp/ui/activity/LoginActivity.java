package com.juno.avoidance.mvp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.github.florent37.materialtextfield.MaterialTextField;

import mehdi.sakout.fancybuttons.FancyButton;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.juno.avoidance.R;
import com.juno.avoidance.di.component.DaggerLoginComponent;
import com.juno.avoidance.mvp.contract.LoginContract;
import com.juno.avoidance.mvp.presenter.LoginPresenter;
import com.juno.avoidance.socket.SocketService;
import com.juno.avoidance.utils.depend.QMUIUtil;

import static com.juno.avoidance.utils.ViewUtil.animate;
import static com.juno.avoidance.utils.ObjectUtil.Again.*;

import java.lang.ref.WeakReference;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * ================================================
 * Description: 登陆界面
 * <p>
 * Created by MVPArmsTemplate on 04/15/2019 20:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindString(R.string.agree)
    String agree;

    @BindView(R.id.btn_login)
    FancyButton loginBtn;

    @BindView(R.id.btn_verify)
    Button verifyBtn;

    @BindView(R.id.mtf_verify)
    MaterialTextField verifyMtf;

    @BindView(R.id.fet_verify)
    FormEditText verifyFet;

    @BindView(R.id.fet_phone)
    FormEditText phoneFet;

    @BindView(R.id.mtf_phone)
    MaterialTextField phoneMtf;

    @BindView(R.id.tv_agree)
    TextView agreeTv;

    /**
     * Created by Juno at 17:37, 2019/4/16.
     * focus description : 点击外部后收回两个输入框
     */
    @OnClick(R.id.root_login)
    public void reduce() {
        verifyMtf.reduce();
        phoneMtf.reduce();
    }

    /**
     * Created by Juno at 15:38, 2019/4/16.
     * verify description : 点击事件：发送验证码
     */
    @OnClick(R.id.btn_verify)
    public void verify(Button v) {
        from(valid(phoneFet.testValidity(), true))
                .send()
                .another(mPresenter)
                .when("request", phoneFet.getText().toString())
                .when("countDown", 60, new WeakReference<>(v))
                .clean();
    }

    /**
     * Created by Juno at 15:38, 2019/4/16.
     * login description : 点击事件：登陆
     */
    @OnClick(R.id.btn_login)
    public void login(View v) {
        //点击动画
        at(() -> animate(v))
                //验证手机号码、验证码格式
                .another(valid(phoneFet.testValidity(), verifyFet.testValidity()))
                .send()
                .another(mPresenter)
                //格式正确则请求登陆
                .when("check", phoneFet.getText().toString(), verifyFet.getText().toString())
                .clean();
    }

    /**
     * Created by Juno at 16:48, 2019/4/19.
     * success description : 长按跳过登陆
     */
    @Override
    @OnLongClick(R.id.btn_login)
    public boolean success() {
        startService(new Intent(this, SocketService.class));
        ArmsUtils.startActivity(HomeActivity.class);
        killMyself();
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        agreeTv.setText(Html.fromHtml(agree));
    }

    /**
     * Created by Juno at 17:14, 2019/4/16.
     * verify description : 展开验证码输入框，并调起输入法
     * {@link LoginPresenter#request(String)}得到返回值后调用
     */
    @Override
    public void verify() {
        verifyMtf.setHasFocus(true);
    }

    /**
     * Created by Juno at 13:44, 2019/4/16.
     * valid description : 点击登陆时控制电话号码、验证码控件的打开与关闭
     */
    private boolean valid(boolean phone, boolean verify) {
        return from(phoneMtf).next(phone ? "reduce" : "expand")
                .another(verifyMtf).next(verify ? "reduce" : "expand")
                .another(phone && verify).get();
    }

    /**
     * Created by Juno at 14:40, 2019/4/16.
     * dialog description : 用于展示加载状态
     */
    private Dialog mDialog = null;

    @Override
    public void showLoading() {
        mDialog = from(mDialog)
                .lazy(() -> QMUIUtil.TipDialog.load(this, "正在加载..."))
                .next("show")
                .get();
    }

    @Override
    public void hideLoading() {
        from(mDialog, "dismiss");
    }

    /**
     * Created by Juno at 14:41, 2019/4/16.
     * showMessage description : 以下是模板自带的方法
     */
    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
