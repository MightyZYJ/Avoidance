package com.juno.avoidance.mvp.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;

import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;
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
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.*;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import timber.log.Timber;

import static com.juno.avoidance.utils.ObjectUtil.Again2.*;
import static com.juno.avoidance.utils.QMUIUtil.Dialog.*;

/**
 * Created by Juno.
 * Date : 2019/4/17.
 * Time : 17:33.
 * When I met you in the summer.
 * Description : 联系人Fragment
 */
public class ContactFragment extends BindFragment implements MultiChoicesCircleButton.OnSelectedItemListener {

    private ContactPresenter presenter;

    @BindView(R.id.rv_contact)
    RecyclerView contactRv;

    @BindView(R.id.mccb_contact_add)
    MultiChoicesCircleButton addMccb;

    @BindString(R.string.regex)
    String regex;

    @BindDrawable(R.drawable.icon_help)
    Drawable help;

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

        //设置RecyclerView
        from(contactRv, "setLayoutManager", new LinearLayoutManager(getContext()))
                //设置Presenter并且调用show()
                .another(presenter)
                .lazy(() -> new ContactPresenter(this))
                .next("show")
                .get(o -> presenter = o)
                //设置3D多选按钮
                .another(new ArrayList<MultiChoicesCircleButton.Item>())
                .next("add", new MultiChoicesCircleButton.Item("添加联系人", help, 90))
                .send()
                .another(addMccb)
                .receive("setButtonItems")
                .next("setOnSelectedItemListener", this)
                .clean();
    }

    /**
     * Created by Juno at 23:17, 2019/4/20.
     * onSelected description : 添加联系人按钮点击事件
     */
    @Override
    public void onSelected(MultiChoicesCircleButton.Item item, int position) {
        QMUIDialog.EditTextDialogBuilder builder = editCommit(getContext(), "修改联系人", "在此输入手机号加昵称", InputType.TYPE_CLASS_TEXT);
        builder.addAction("确定", (dialog, index) -> {
            String text = builder.getEditText().getText().toString();
            new StringChain(text).begin(() -> {
                presenter.add(text.substring(11), text.substring(0, 11));
                dialog.dismiss();
            });
        }).create().show();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private QMUITipDialog dialog = null;

    public void showLoading() {
        dialog = from(dialog)
                .lazy(() -> QMUIUtil.TipDialog.load(getContext(), "正在加载..."))
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
                EditTextDialogBuilder builder = editCommit(getContext(), "修改联系人", "在此输入手机号加昵称", InputType.TYPE_CLASS_TEXT).setDefaultText(contact.getPhone() + contact.getName());
                builder.addAction("确定", (dialog, index) -> {
                    String text = builder.getEditText().getText().toString();
                    new StringChain(text).begin(() -> {
                        contact.setName(text.substring(11));
                        contact.setPhone(text.substring(0, 11));
                        presenter.update(contact);
                        dialog.dismiss();
                    });
                }).create().show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                cancelCommit(getContext(), "标题", "确定要删除吗？", (QMUIUtil.Dialog.CommitAction) () -> {
                    presenter.delete(contacts.get(position).getId());
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

    /**
     * Created by Juno at 16:37, 2019/4/21.
     * StringChain description : 处理字符串
     */
    private class StringChain extends ChainUtil.EventStream {
        private String text;

        private StringChain(String text) {
            this.text = text;
        }

        private void begin(Celebration celebration) {
            stopWhen(true)
                    .when(() -> text.length() < 11)
                    .react(() -> showAndLog("输入过短"))
                    .when(() -> text.length() > 20)
                    .react(() -> showAndLog("输入过长"))
                    .when(() -> !text.substring(0, 11).matches(regex))
                    .react(() -> showAndLog("请检查手机号码"))
                    .doWhenSuccess(celebration)
                    .flow();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
        contactRv.setAdapter(null);
    }


}
