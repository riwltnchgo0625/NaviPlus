package com.example.convenientfacilities_example.Retrofit;


import com.example.convenientfacilities_example.Model.WeatherForecastResult;
import com.example.convenientfacilities_example.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IopenWeatherMap {

    @GET ("weather")
    Observable<WeatherResult> getWeatherByLacing(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit);


    @GET ("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String cityName,
                                                   @Query("appid") String appid,
                                                   @Query("units") String unit);


    @GET ("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLacing(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit);
}
