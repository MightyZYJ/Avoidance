package com.juno.avoidance.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.juno.avoidance.di.module.LoginModule;
import com.juno.avoidance.mvp.contract.LoginContract;

import com.jess.arms.di.scope.ActivityScope;
import com.juno.avoidance.mvp.ui.activity.LoginActivity;


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
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {
    void inject(LoginActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LoginComponent.Builder view(LoginContract.View view);

        LoginComponent.Builder appComponent(AppComponent appComponent);

        LoginComponent build();
    }
}