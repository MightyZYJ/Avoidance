package com.juno.avoidance.mvp.model.api.service;

import com.juno.avoidance.mvp.model.entity.Contact;
import com.juno.avoidance.mvp.model.entity.http.Request;
import com.juno.avoidance.mvp.model.entity.http.Response;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Juno.
 * Date : 2019/4/20.
 * Time : 19:03.
 * When I met you in the summer.
 * Description : 联系人增删查改
 */
public interface ContactService {

    String CONTACT = "/contact";
    String HEADER_JSON = "Content-Type:application/json";

    @Headers(HEADER_JSON)
    @POST(CONTACT + "/add")
    Observable<Response> add(@Body Request.RequestAddContact addContact);

    @Headers(HEADER_JSON)
    @POST(CONTACT + "/delete")
    Observable<Response> delete(@Body Request.RequestDeleteContact deleteContact);

    @GET(CONTACT + "/show")
    Observable<Response.ResponseContacts> show();

    @Headers(HEADER_JSON)
    @POST(CONTACT + "/update")
    Observable<Response> update(@Body Contact contact);

}
