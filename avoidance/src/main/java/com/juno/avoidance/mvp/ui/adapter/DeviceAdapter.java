package com.juno.avoidance.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jess.arms.utils.ArmsUtils;
import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.Device;
import com.juno.avoidance.mvp.ui.activity.MapActivity;
import com.juno.avoidance.utils.ObjectUtil;
import com.juno.avoidance.utils.QMUIUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.juno.avoidance.utils.ObjectUtil.Again.at;
import static com.juno.avoidance.utils.ObjectUtil.notNull;
import static com.juno.avoidance.utils.ViewUtil.animate;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 9:36.
 * When I met you in the summer.
 * Description : 设备的适配器和点击监听
 */
public class DeviceAdapter extends CommonAdapter<Device> {

    private String active;
    private String inactive;

    public DeviceAdapter(Context context, List<Device> datas) {
        super(context, R.layout.item_device, datas);
        active = mContext.getString(R.string.active);
        inactive = mContext.getString(R.string.inactive);
    }

    @Override
    protected void convert(ViewHolder holder, Device device, int position) {
        holder.setText(R.id.tv_device_title, device.getTitle()); //标题
        holder.setText(R.id.tv_device_description, device.getDescription()); //简介
        Glide.with(mContext).load(device.getUrl()).into((ImageView) holder.getView(R.id.iv_device_image)); //图片
        ((TextView) holder.getView(R.id.tv_device_state)).setText(device.isState() ? Html.fromHtml(active) : Html.fromHtml(inactive)); //状态
    }

    public static class Listener implements MultiItemTypeAdapter.OnItemClickListener {

        private WeakReference<List<Device>> mData;
        private WeakReference<Context> mContext;

        public Listener(Context context, List<Device> data) {
            this.mContext = new WeakReference<>(context);
            this.mData = new WeakReference<>(data);
        }

        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            animate(view);

            Context context = mContext.get();
            List<Device> data = mData.get();

            if (notNull(context) && notNull(data)) {
                at(data.get(position).isState() ?
                        () -> ArmsUtils.startActivity(MapActivity.class) :
                        () -> QMUIUtil.TipDialog.info(context, "设备未登陆").show());
            }

        }

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    }

}
