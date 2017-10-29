package com.packtpub.apps.rxjava_essentials.chapter8.api.openweathermap;

import com.packtpub.apps.rxjava_essentials.chapter8.api.openweathermap.models.WeatherResponse;

import io.reactivex.Observable;
import retrofit.http.GET;
import retrofit.http.Query;


public interface OpenWeatherMapService {

    @GET("/data/2.5/weather")
    Observable<WeatherResponse> getForecastByCity(@Query("q") String city);
}
