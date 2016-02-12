package com.andrerichards.andre.weather;

import com.andrerichards.andre.weather.com.andrerichards.andre.weather.GetWeatherObjects;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Url;

/**
 * Created by Andre on 12/27/2015.
 */

public interface WeatherServiceAPI {
    @GET
    Call<GetWeatherObjects> JSONObjects(@Url String url);
}