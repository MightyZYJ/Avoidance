package com.juno.avoidance.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.juno.avoidance.mvp.contract.LoginContract;
import com.juno.avoidance.mvp.model.api.service.LoginService;
import com.juno.avoidance.mvp.model.entity.http.Request;
import com.juno.avoidance.mvp.model.entity.http.Response;

import io.reactivex.Observable;
import timber.log.Timber;


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
public class LoginModel extends BaseModel implements LoginContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<Response> check(String phone, String verify) {
        Timber.e(new Request.RequestCheck(phone, verify).toString());
        return mRepositoryManager.obtainRetrofitService(LoginService.class)
                .check(new Request.RequestCheck(phone, verify));
    }

    @Override
    public Observable<Response> request(String phone) {
        return mRepositoryManager.obtainRetrofitService(LoginService.class)
                .request(new Request.RequestVerify(phone));
    }
}