package com.juno.avoidance.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.Contact;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 21:45.
 * When I met you in the summer.
 * Description : 联系人的适配器
 */
public class ContactAdapter extends CommonAdapter<Contact> {

    public ContactAdapter(Context context, List<Contact> datas) {
        super(context, R.layout.item_contact, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Contact contact, int position) {
        holder.setText(R.id.tv_contact_name, contact.getName());
    }
}
