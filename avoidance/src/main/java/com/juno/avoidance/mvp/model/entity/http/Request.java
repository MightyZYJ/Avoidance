package com.juno.avoidance.mvp.model.entity.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 14:14.
 * When I met you in the summer.
 * Description :
 */
public class Request {

    @Data
    @AllArgsConstructor
    public static class RequestVerify {
        private String phone;
    }

    @Data
    @AllArgsConstructor
    public static class RequestCheck {
        private String phone;
        private String authCode;
    }

    @Data
    @AllArgsConstructor
    public static class RequestAddContact {
        private String name;
        private String phone;
    }

    @Data
    @AllArgsConstructor
    public static class RequestDeleteContact {
        private int id;
    }

}
