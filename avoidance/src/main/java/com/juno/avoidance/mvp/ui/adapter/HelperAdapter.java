package com.juno.avoidance.mvp.ui.adapter;

import android.content.Context;

import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.Helper;
import com.suke.widget.SwitchButton;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import timber.log.Timber;

import static com.juno.avoidance.utils.ObjectUtil.Again.*;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 10:05.
 * When I met you in the summer.
 * Description : 救援项目的适配器
 */
public class HelperAdapter extends CommonAdapter<Helper> implements SwitchButton.OnCheckedChangeListener {

    public HelperAdapter(Context context, List<Helper> datas) {
        super(context, R.layout.item_help, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Helper helper, int position) {
        from(holder).cache(true)
                .next("setText", R.id.tv_help_name, helper.getName())
                .next("setVisible", R.id.tv_help_active, !helper.isUsable())
                .next("setVisible", R.id.sb_help, helper.isUsable())
                .args(helper.isUsing())
                .when("setChecked", R.id.sb_help, helper.isUsing())
                .map((Mapper<ViewHolder, SwitchButton>) viewHolder -> holder.getView(R.id.sb_help))
                .clean()
                .next("setOnCheckedChangeListener", this);
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        Timber.e(isChecked ? "开启" : "关闭");
    }
}
