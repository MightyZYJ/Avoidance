package com.juno.avoidance.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.juno.avoidance.di.module.CustomizeModule;
import com.juno.avoidance.mvp.contract.CustomizeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.juno.avoidance.mvp.ui.activity.CustomizeActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/18/2019 15:18
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = CustomizeModule.class, dependencies = AppComponent.class)
public interface CustomizeComponent {
    void inject(CustomizeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CustomizeComponent.Builder view(CustomizeContract.View view);

        CustomizeComponent.Builder appComponent(AppComponent appComponent);

        CustomizeComponent build();
    }
}