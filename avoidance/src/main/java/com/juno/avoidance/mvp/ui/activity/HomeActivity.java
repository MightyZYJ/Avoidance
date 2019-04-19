package com.juno.avoidance.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.juno.avoidance.di.component.DaggerHomeComponent;
import com.juno.avoidance.mvp.contract.HomeContract;
import com.juno.avoidance.mvp.presenter.HomePresenter;

import com.juno.avoidance.R;
import com.juno.avoidance.mvp.ui.fragment.ContractFragment;
import com.juno.avoidance.mvp.ui.fragment.DeviceFragment;
import com.juno.avoidance.mvp.ui.fragment.HelpFragment;
import com.juno.avoidance.utils.FragmentUtil;
import com.juno.avoidance.utils.QMUIUtil;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindView;
import static com.juno.avoidance.utils.ObjectUtil.*;
import static com.juno.avoidance.utils.ObjectUtil.Again.*;


/**
 * ================================================
 * Description: 主界面
 * <p>
 * Created by MVPArmsTemplate on 04/16/2019 21:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @BindArray(R.array.tabs)
    String[] tabs;

    @BindColor(R.color.colorPrimary)
    int blue;

    @BindView(R.id.tv_home_title)
    TextView titleTv;

    @BindView(R.id.bnv_home)
    BottomNavigationView homeBnv;

    //need release
    private FragmentUtil.FragmentChain fragmentChain;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        at(() -> QMUIStatusBarHelper.translucent(HomeActivity.this)) //沉浸式
                .another(homeBnv) //设置底部导航栏
                .cache(true)
                .next("addTab", new BottomNavigationItem(tabs[0], blue, R.drawable.icon_hardware_fill))
                .same(new BottomNavigationItem(tabs[1], blue, R.drawable.icon_group))
                .same(new BottomNavigationItem(tabs[2], blue, R.drawable.icon_help))
                .next("setOnBottomNavigationItemClickListener", (OnBottomNavigationItemClickListener) this::page)
                .another(titleTv) //设置标题
                .next("setText", tabs[0])
                .next("setTranslationY", QMUIStatusBarHelper.getStatusbarHeight(this) / 3f)
                .another(fragmentChain) //设置Fragment
                .lazy(FragmentUtil.FragmentChain::new)
                .cache(true)
                .next("manager", getSupportFragmentManager())
                .next("add", new DeviceFragment())
                .same(new ContractFragment())
                .same(new HelpFragment())
                .next("container", R.id.fragment_root)
                .next("show")
                .get((Getter<FragmentUtil.FragmentChain>) o -> fragmentChain = o);
    }

    private QMUITipDialog mDialog = null;

    /**
     * Created by Juno at 9:01, 2019/4/17.
     * page description : 跳转到某页面
     */
    private void page(@IntRange(from = 0, to = 2) int index) {
        titleTv.setText(tabs[index]);
        fragmentChain.show(index);
    }

    @Override
    public void showLoading() {
        mDialog = Again.from(mDialog)
                .lazy(() -> QMUIUtil.load(this, "正在加载..."))
                .next("show")
                .get();
    }

    @Override
    public void hideLoading() {
        from(mDialog, "dismiss");
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentChain.destroy();
        fragmentChain = null;
    }
}
