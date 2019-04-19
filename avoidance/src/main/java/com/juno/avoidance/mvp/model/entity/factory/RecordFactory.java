package com.juno.avoidance.mvp.model.entity.factory;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.juno.avoidance.mvp.model.entity.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 13:45.
 * When I met you in the summer.
 * Description :
 */
public class RecordFactory {

    private static final LatLonPoint MY_LOCATION = new LatLonPoint(23.035219, 113.398205);

    public static List<Record> create() {
        List<Record> records = new ArrayList<>();
        records.add(new Record(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude(), "广东工业大学工学一号馆", new Date()));
        records.add(new Record(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude(), "广东工业大学工学二号馆", new Date()));
        records.add(new Record(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude(), "广东工业大学工学三号馆", new Date()));
        records.add(new Record(MY_LOCATION.getLatitude(), MY_LOCATION.getLongitude(), "广东工业大学工学四号馆", new Date()));
        return records;
    }

}
