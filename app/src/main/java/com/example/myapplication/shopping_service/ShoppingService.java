package com.example.myapplication.shopping_service;

import com.example.myapplication.Login;
import com.example.myapplication.ScrollingActivity;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ShoppingService {
    String BASE_URL = "http://10.0.0.71:5005";

    @GET("/list")
    Call<JsonObject> getItems();

    @POST("/register")
    Call<String> sendRegisterinLogs(@Body Login loginRequest);

    @POST("/login")
    Call<String> sendLoginLogs(@Body Login loginRequest);

    @POST("/buy")
    Call<String> sendPurchaseRequest(@Body ScrollingActivity.ShopData purchaseRequest);

}
