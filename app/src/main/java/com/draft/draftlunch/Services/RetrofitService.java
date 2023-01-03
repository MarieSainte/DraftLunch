package com.draft.draftlunch.Services;


import com.draft.draftlunch.Models.DetailRestaurant;
import com.draft.draftlunch.Models.Nearby;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {



    @GET("maps/api/place/nearbysearch/json")
    Observable<Nearby> getRestaurants(@Query("location") String location,
                                      @Query("radius") int radius,
                                      @Query("type") String type,
                                      @Query("keyword") String keyword,
                                      @Query("key") String key);

    @GET("https://maps.googleapis.com/maps/api/place/details/json")
    Observable<DetailRestaurant> getDetail(@Query("place_id") String place_id,
                                            @Query("fields") String fields,
                                            @Query("key")String key);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(RestaurantRepository.getHttpClient())
            .build();
}
