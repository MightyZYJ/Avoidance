package com.juno.avoidance.mvp.model.entity.factory;

import com.juno.avoidance.mvp.model.entity.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 10:36.
 * When I met you in the summer.
 * Description :
 */
public class HelperFactory {

    public static List<Helper> create() {
        List<Helper> helpers = new ArrayList<>();

        helpers.add(new Helper("短信求救", true, true));
        helpers.add(new Helper("联系公安", true, false));
        helpers.add(new Helper("第三方救援团队", false, false));
        helpers.add(new Helper("联系我的医生", false, false));

        return helpers;
    }

}
