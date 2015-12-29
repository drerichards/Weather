package com.andrerichards.andre.weather;

import com.andrerichards.andre.weather.com.andrerichards.andre.weather.GetWeatherObjects;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Andre on 12/27/2015.
 */
public interface WeatherServiceAPI {

    @GET("/data/2.5/weather?q=miami&APPID=ef4fe2ec9c96d75eb824cf8e9b2cf61a")
    Call<GetWeatherObjects> listJSONObjects();
}