package com.juno.avoidance.mvp.model.api.service;

import com.juno.avoidance.mvp.model.entity.http.Request;
import com.juno.avoidance.mvp.model.entity.http.Response;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 13:53.
 * When I met you in the summer.
 * Description :
 */
public interface LoginService {

    String LOGIN = "/login";
    String HEADER_JSON = "Content-Type:application/json";

    @Headers(HEADER_JSON)
    @POST(LOGIN + "/request")
    Observable<Response> request(@Body Request.RequestVerify requestVerify);

    @Headers(HEADER_JSON)
    @POST(LOGIN + "/check")
    Observable<Response> check(@Body Request.RequestCheck requestCheck);

}
