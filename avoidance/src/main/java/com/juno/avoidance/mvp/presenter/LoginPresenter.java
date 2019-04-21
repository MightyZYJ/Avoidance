package com.juno.avoidance.mvp.presenter;

import android.app.Application;
import android.widget.Button;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jess.arms.utils.RxLifecycleUtils;
import com.juno.avoidance.mvp.contract.LoginContract;
import com.juno.avoidance.mvp.model.entity.http.Response;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/15/2019 20:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * Created by Juno at 15:22, 2019/4/16.
     * check description : 登陆
     */
    public void check(String phone, String verify) {

        mModel.check(phone, verify)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))
                .doOnSubscribe(disposable -> mRootView.showLoading())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mRootView.hideLoading())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Response>(mErrorHandler) {
                    @Override
                    public void onNext(Response response) {
                        if (response.to() == Response.StateCodeEnum.OK) {
                            mRootView.success();
                        } else {
                            mRootView.showMessage(response.getInfo());
                        }
                    }
                });
    }

    /**
     * Created by Juno at 15:24, 2019/4/16.
     * request description : 发送验证码
     */
    public LoginPresenter request(String phone) {

        mModel.request(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(1, 2))
                .doOnSubscribe(disposable -> mRootView.showLoading())
                .doFinally(() -> mRootView.hideLoading())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Response>(mErrorHandler) {
                    @Override
                    public void onNext(Response response) {
                        mRootView.showMessage(response.to() == Response.StateCodeEnum.OK ? "验证码已发送，注意查看短信" : response.getInfo());
                        mRootView.verify();
                    }
                });
        return this;
    }

    /**
     * Created by Juno at 15:41, 2019/4/16.
     * countDown description : 倒数
     */
    public void countDown(final int time, WeakReference<Button> wrfBtn) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map((Function<Long, Object>) aLong -> time - aLong.intValue())
                .take(time + 1)
                .map(Object::toString)
                .doOnSubscribe(disposable -> button(wrfBtn, false))
                .doFinally(() -> button(wrfBtn, true, "发送验证码"))
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String s) {
                        button(wrfBtn, s + "(s)");
                    }
                });
    }

    /**
     * Created by Juno at 16:59, 2019/4/16.
     * button description : 设置按钮的text或enable
     */
    private static void button(WeakReference<Button> wrfBtn, Object... objects) {
        for (Object o : objects) {
            Button button = wrfBtn.get();
            if (button != null) {
                if (o instanceof String) {
                    button.setText(o.toString());
                } else if (o instanceof Boolean) {
                    button.setEnabled((Boolean) o);
                }
            }
        }
    }

}
