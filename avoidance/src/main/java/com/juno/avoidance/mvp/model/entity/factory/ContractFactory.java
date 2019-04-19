package com.juno.avoidance.mvp.model.entity.factory;

import com.juno.avoidance.mvp.model.entity.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 21:43.
 * When I met you in the summer.
 * Description :
 */
public class ContractFactory {

    public static List<Contract> create() {
        List<Contract> contracts = new ArrayList<>();
        contracts.add(new Contract(1, "张艺隽", "17688212307"));
        contracts.add(new Contract(2, "吉臻伟", "17688212307"));
        contracts.add(new Contract(3, "许锦彬", "17688212307"));
        contracts.add(new Contract(4, "林宗展", "17688212307"));
        contracts.add(new Contract(5, "黄伟峰", "17688212307"));
        return contracts;
    }

}
