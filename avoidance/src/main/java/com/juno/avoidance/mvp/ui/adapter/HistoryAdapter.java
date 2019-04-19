package com.juno.avoidance.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.juno.avoidance.R;
import com.juno.avoidance.mvp.model.entity.Record;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 14:07.
 * When I met you in the summer.
 * Description :
 */
public class HistoryAdapter extends CommonAdapter<Record> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy年MM月dd日-HH : mm");

    public HistoryAdapter(Context context, List<Record> datas) {
        super(context, R.layout.item_history, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Record record, int position) {
        holder.setText(R.id.tv_history_location, record.getLocation());
        holder.setText(R.id.tv_history_date, FORMAT.format(record.getDate()));
    }
}
