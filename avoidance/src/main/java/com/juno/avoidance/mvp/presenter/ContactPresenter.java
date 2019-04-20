package com.juno.avoidance.mvp.presenter;
import com.jess.arms.utils.ArmsUtils;
import com.juno.avoidance.mvp.model.api.service.ContactService;
import com.juno.avoidance.mvp.model.entity.Contact;
import com.juno.avoidance.mvp.model.entity.http.Request;
import com.juno.avoidance.mvp.model.entity.http.Response;
import com.juno.avoidance.mvp.model.entity.msg.MsgContact;
import com.juno.avoidance.mvp.ui.fragment.ContactFragment;
import com.juno.avoidance.utils.RxUtil;

import org.greenrobot.eventbus.EventBus;
import java.util.List;
import java.util.Objects;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import static com.juno.avoidance.utils.ObjectUtil.notNull;

/**
 * Created by Juno.
 * Date : 2019/4/20.
 * Time : 19:38.
 * When I met you in the summer.
 * Description :
 */
public class ContactPresenter {

    private ContactService service;
    private ContactFragment mFragment;

    public ContactPresenter(ContactFragment fragment) {
        service = ArmsUtils.obtainAppComponentFromContext(Objects.requireNonNull(fragment.getContext()))
                .repositoryManager()
                .obtainRetrofitService(ContactService.class);
        mFragment = fragment;
    }

    /**
     * Created by Juno at 20:22, 2019/4/20.
     * add description : 添加联系人
     */
    public void add(String name, String phone) {
        execute(service.add(new Request.RequestAddContact(name, phone)), "添加");
        show();
    }

    /**
     * Created by Juno at 20:22, 2019/4/20.
     * delete description : 删除联系人
     */
    public void delete(int id) {
        execute(service.delete(new Request.RequestDeleteContact(id)), "删除");
        show();
    }

    /**
     * Created by Juno at 20:22, 2019/4/20.
     * update description : 修改联系人
     */
    public void update(Contact contact) {
        execute(service.update(contact), "修改");
        show();
    }

    /**
     * Created by Juno at 20:22, 2019/4/20.
     * show description : 显示联系人
     */
    public void show() {
        service.show()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mFragment.showLoading())
                .doFinally(() -> mFragment.hideLoading())
                .subscribe(new RxUtil.SimpleObserver<Response.ResponseContacts>() {
                    @Override
                    public void onNext(Response.ResponseContacts responseContacts) {
                        List<Contact> contacts = responseContacts.getData();
                        if (notNull(contacts)) {
                            EventBus.getDefault().post(contacts);
                        }
                    }
                });
    }

    /**
     * Created by Juno at 20:21, 2019/4/20.
     * execute description : 抽离重复代码
     */
    private void execute(Observable<Response> observable, String action) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mFragment.showLoading())
                .doFinally(() -> mFragment.hideLoading())
                .subscribe(new RxUtil.SimpleObserver<Response>() {
                    @Override
                    public void onNext(Response response) {
                        if (response.to() == Response.StateCodeEnum.OK) {
                            EventBus.getDefault().post(new MsgContact(action + "成功"));
                            show();
                        } else {
                            EventBus.getDefault().post(new MsgContact(action + "失败"));
                        }
                    }
                });
    }

    public void destroy() {
        service = null;
        mFragment = null;
    }

}
