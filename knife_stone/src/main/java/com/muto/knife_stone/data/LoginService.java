package com.muto.knife_stone.data;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by ArcSoft on 2016/3/3.
 */
public interface LoginService {
    @POST("/login")
    Call<User> basicLogin();
}
