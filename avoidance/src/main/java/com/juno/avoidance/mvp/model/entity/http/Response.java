package com.juno.avoidance.mvp.model.entity.http;

import lombok.Data;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 14:01.
 * When I met you in the summer.
 * Description :
 */
@Data
public class Response {

    private int state;
    private String info;

    public StateCodeEnum to() {
        return StateCodeEnum.from(state);
    }

    public enum StateCodeEnum {
        OK(200, "操作正常"),
        PARAMS_NULL(501, "参数为空"),
        PHONE_NOT_EXIST(51, "电话不存在"),
        VERIFICATION_CODE_ERROR(52, "验证码错误"),
        NAME_TOO_LONG(53, "联系⼈名称太⻓"),
        PHONE_TOO_LONG(54, "电话太⻓"),
        CONTACT_NOT_EXIST(55, "联系⼈不存在"),
        NEED_ALARM(56, "需要报警"),
        NOT_NEED_ALARM(57, "不需要报警"),
        UNKNOWN(-1, "未知状态");

        StateCodeEnum(int code, String info) {
        }

        public static StateCodeEnum from(int state) {
            switch (state) {
                case 200:
                    return OK;
                case 501:
                    return PARAMS_NULL;
                case 51:
                    return PHONE_NOT_EXIST;
                case 52:
                    return VERIFICATION_CODE_ERROR;
                case 53:
                    return NAME_TOO_LONG;
                case 54:
                    return PHONE_TOO_LONG;
                case 55:
                    return CONTACT_NOT_EXIST;
                case 56:
                    return NEED_ALARM;
                case 57:
                    return NOT_NEED_ALARM;
                default:
                    return UNKNOWN;
            }
        }
    }

}
