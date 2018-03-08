package com.williamgdev.example.googleplaceapi;


import com.williamgdev.example.googleplaceapi.dto.PlaceDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GPAWebServiceApi {

    @GET("place/details/json?{placeid}&{key}")
    Call<PlaceDetailsResponse> getPlaceDetails(@Path("placeid") String placeId, @Path("key") String apiKey);
}