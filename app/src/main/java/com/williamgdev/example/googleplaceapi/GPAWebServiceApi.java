package com.williamgdev.example.googleplaceapi;


import com.williamgdev.example.googleplaceapi.dto.PlaceDetailsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GPAWebServiceApi {

    @GET("place/details/json")
    Call<PlaceDetailsResponse> getPlaceDetails(@QueryMap Map<String, String> data);
}