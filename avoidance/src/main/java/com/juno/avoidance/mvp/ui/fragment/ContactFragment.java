package com.juno.avoidance.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.Contact;
import com.juno.avoidance.mvp.model.entity.msg.MsgContact;
import com.juno.avoidance.mvp.presenter.ContactPresenter;
import com.juno.avoidance.mvp.ui.adapter.ContactAdapter;
import com.juno.avoidance.mvp.ui.fragment.base.BindFragment;
import com.juno.avoidance.utils.ChainUtil;
import com.juno.avoidance.utils.QMUIUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import timber.log.Timber;

import static com.juno.avoidance.utils.ObjectUtil.Again.from;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 17:33.
 * When I met you in the summer.
 * Description : 联系人Fragment
 */
public class ContactFragment extends BindFragment {

    private ContactPresenter presenter;

    @BindView(R.id.rv_contact)
    RecyclerView contactRv;

    @BindString(R.string.regex)
    String regex;

    @Override
    protected int layout() {
        return R.layout.fragment_contact;
    }

    @Override
    protected boolean bindView() {
        return true;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        contactRv.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter = new ContactPresenter(this);
        presenter.show();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private QMUITipDialog dialog = null;

    public void showLoading() {
        dialog = from(dialog)
                .lazy(() -> QMUIUtil.load(getContext(), "正在加载..."))
                .next("show")
                .get();
    }

    public void hideLoading() {
        from(dialog, "dismiss");
    }

    /**
     * Created by Juno at 20:16, 2019/4/20.
     * showMsg description : 展示信息
     */
    @Subscribe
    public void showMsg(MsgContact msgContact) {
        ArmsUtils.snackbarText(msgContact.info);
    }

    /**
     * Created by Juno at 20:16, 2019/4/20.
     * showContacts description : 刷新列表
     */
    @Subscribe
    public void showContacts(final List<Contact> contacts) {

        ContactAdapter adapter = new ContactAdapter(getContext(), contacts);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Contact contact = contacts.get(position);
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
                builder.setTitle("修改联系人")
                        .setPlaceholder("在此输入手机号加昵称")
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .setDefaultText(contact.getPhone() + contact.getName())
                        .addAction("取消", (dialog, index) -> dialog.dismiss())
                        .addAction("确定", (dialog, index) -> {

                            String text = builder.getEditText().getText().toString();

                            ChainUtil.begin().stopWhen(true)
                                    .when(() -> text.length() < 11)
                                    .react(() -> showAndLog("输入过短"))

                                    .when(() -> text.length() > 20)
                                    .react(() -> showAndLog("输入过长"))

                                    .when(() -> !text.substring(0, 11).matches(regex))
                                    .react(() -> showAndLog("请检查手机号码"))

                                    .doWhenSuccess(() -> {
                                        contact.setName(text.substring(11));
                                        contact.setPhone(text.substring(0, 11));
                                        presenter.update(contact);
                                        dialog.dismiss();
                                    }).flow();

                        }).create().show();

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {

                int id = contacts.get(position).getId();

                new QMUIDialog.MessageDialogBuilder(getActivity())
                        .setTitle("标题")
                        .setMessage("确定要删除吗？")
                        .addAction("取消", (dialog, index) -> dialog.dismiss())
                        .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                            presenter.delete(id);
                            dialog.dismiss();
                        }).show();
                return false;
            }
        });
        contactRv.setAdapter(adapter);
    }

    private void showAndLog(String s) {
        ArmsUtils.snackbarText(s);
        Timber.e(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
        contactRv.setAdapter(null);
    }
}
