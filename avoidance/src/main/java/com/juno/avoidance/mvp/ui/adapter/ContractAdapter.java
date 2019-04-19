package com.juno.avoidance.mvp.ui.adapter;

import android.content.Context;

import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.Contract;
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
public class ContractAdapter extends CommonAdapter<Contract> {

    public ContractAdapter(Context context, List<Contract> datas) {
        super(context, R.layout.item_contract, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Contract contract, int position) {
        holder.setText(R.id.tv_contract_name, contract.getName());
    }
}
