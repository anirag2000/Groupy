package com.example.groupy.Messenger;

import com.example.groupy.Service.MyResponse;
import com.example.groupy.Service.Sender;

import retrofit2.http.Headers;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAaSd9fcA:APA91bEvCaDJKg6eoj9DtDh0Jj2hT4iRh8HWzDKrXXSa2DUFk6To6yelEQNAcYtZnRXsVbR6Ro12LSV_ENgIWsan9UjE8kp1GylP0sqtvY3lLwQ7LmfYMdMZb3ACvratgklBYznfW0kD"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}