package com.juno.avoidance.mvp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 21:40.
 * When I met you in the summer.
 * Description : 联系人
 */
@Data
@AllArgsConstructor
public class Contract {

    private int id;
    private String name;
    private String phone;

}
