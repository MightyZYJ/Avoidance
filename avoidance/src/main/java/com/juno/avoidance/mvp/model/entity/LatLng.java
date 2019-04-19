package com.juno.avoidance.mvp.model.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 13:52.
 * When I met you in the summer.
 * Description :
 */
@Data
public class LatLng implements Serializable {

    private double latitude;
    private double longitude;

}
