package com.juno.avoidance.mvp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.juno.avoidance.di.component.DaggerCustomizeComponent;
import com.juno.avoidance.mvp.contract.CustomizeContract;
import com.juno.avoidance.mvp.model.entity.msg.MsgTitle;
import com.juno.avoidance.mvp.presenter.CustomizePresenter;

import com.juno.avoidance.R;
import com.juno.avoidance.utils.depend.QMUIUtil;
import com.juno.avoidance.utils.depend.SlidrUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.r0adkll.slidr.Slidr;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.juno.avoidance.utils.ObjectUtil.Again.*;


/**
 * ================================================
 * Description: 自定义设备界面
 * <p>
 * Created by MVPArmsTemplate on 04/18/2019 15:18
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class CustomizeActivity extends BaseActivity<CustomizePresenter> implements CustomizeContract.View {

    @BindView(R.id.tv_custom_title)
    TextView titleTv;

    @BindView(R.id.iv_custom_back)
    ImageView backIv;

    @BindView(R.id.tv_device_name)
    TextView deviceNameTv;

    @BindView(R.id.tv_function)
    TextView functionTv;

    @BindView(R.id.cb_agree)
    CheckBox agreeCb;

    @BindView(R.id.et_device_name)
    EditText deviceNameEt;

    @OnClick({R.id.tv_location, R.id.tv_message, R.id.tv_alarm, R.id.tv_longitude, R.id.tv_latitude, R.id.iv_add_data})
    public void pleaseWait() {
        QMUIUtil.Dialog.singleCommit(this, "消息", "功能完善中，敬请期待！", true).show();
    }

    @OnClick(R.id.btn_commit)
    public void commit() {
        QMUIUtil.Dialog.singleCommit(this, "申请成功", "你的设备正在审核中，3个工作日将会收到结果，请留意短信通知", false, this::killMyself).show();
    }

    @BindArray(R.array.text)
    String[] text;

    @OnClick({R.id.iv_custom_back, R.id.btn_cancel})
    void back() {
        killMyself();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCustomizeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_customize; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        at(() -> QMUIStatusBarHelper.translucent(this))
                .still(() -> Slidr.attach(this, SlidrUtil.config(this)))
                .another(getIntent().getSerializableExtra(MsgTitle.M))
                .map(serializable -> ((MsgTitle) serializable).title)
                .send()
                .another(backIv, "setTranslationY", QMUIStatusBarHelper.getStatusbarHeight(this) / 3f)
                .another(titleTv, "setTranslationY", QMUIStatusBarHelper.getStatusbarHeight(this) / 3f)
                .receive("setText")
                .another(deviceNameEt)
                .receive("setText")
                .another(deviceNameTv, "setText", Html.fromHtml(text[0]))
                .another(functionTv, "setText", Html.fromHtml(text[1]))
                .another(agreeCb, "setText", Html.fromHtml(text[2]))
                .fork()
                .setBefore((proxy, method, args) -> {
                    Timber.e(method.getName());
                    return true;
                })
                .get()
                .next("setChecked", true)
                .clean();

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
     * Created by Juno at 15:25, 2019/4/18.
     * showMessage description : 以下为模板自动生成的方法
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
