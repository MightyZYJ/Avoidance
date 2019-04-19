package com.juno.avoidance.mvp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Juno.
 * Date : 2019/4/18.
 * Time : 23:39.
 * When I met you in the summer.
 * Description :
 */
@Data
@AllArgsConstructor
public class Helper {
    private String name;
    private boolean isUsable;
    private boolean isUsing;
}
