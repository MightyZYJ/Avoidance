package com.juno.avoidance.mvp.model.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 13:22.
 * When I met you in the summer.
 * Description : 求救信号
 */
@Data
@AllArgsConstructor
public class Record {

    private double latitude;
    private double longitude;
    private String location;
    private Date date;

}
