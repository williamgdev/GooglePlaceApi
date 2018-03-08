package com.williamgdev.example.googleplaceapi;


import android.content.Context;

import com.williamgdev.example.googleplaceapi.dto.PlaceDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GPAWebServiceInteractor {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private static String WEB_SERVICE_API_KEY;
    private static GPAWebServiceInteractor instance;
    private static GPAWebServiceApi apiService;

    public static GPAWebServiceInteractor getInstance(Context context) {
        if (instance == null) {
            instance = new GPAWebServiceInteractor();
            WEB_SERVICE_API_KEY = context.getString(R.string.google_api_web_service_key);
        }
        return instance;
    }

    private GPAWebServiceInteractor(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GPAWebServiceApi.class);
    }

    public void getPlaceDetails(String placeId, GPAWebServiceInteractorListener<PlaceDetailsResponse> listener) {
        apiService.getPlaceDetails(placeId, WEB_SERVICE_API_KEY).enqueue(getPlaceDetailsCallBack(listener));
    }

    private Callback<PlaceDetailsResponse> getPlaceDetailsCallBack(final GPAWebServiceInteractorListener<PlaceDetailsResponse> listener) {
        return new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                switch (response.code()) {
                    case GPAWebServiceInteractorListener.OK:
                        listener.onSuccess(response.body());
                        break;
                    default:
                        listener.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        };
    }

    public interface GPAWebServiceInteractorListener<T> extends BaseApiListener{
        void onSuccess(T result);
        void onError(String text);
    }

    public interface BaseApiListener {
        int OK = 200;
        int BAD_REQUEST = 400;
        int SERVER_ERROR = 500;
        int CREATED = 201;
        int NOT_FOIND = 404;
        int CONFLICT = 409;
        int UNAUTHORIZED = 401;
        /**
         * TODO add all code here
         * http://www.restpatterns.org/HTTP_Status_Codes/
         */
    }
}
