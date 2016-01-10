package com.andrerichards.andre.weather;

import com.andrerichards.andre.weather.com.andrerichards.andre.weather.GetWeatherObjects;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Andre on 12/27/2015.
 */

public interface WeatherServiceAPI {

    @GET("/weather?q={name}")
    Call<GetWeatherObjects> listJSONObjects(@Query("name") String name);

}