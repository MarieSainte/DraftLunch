package com.draft.draftlunch.Services;

import android.util.Log;

import com.draft.draftlunch.Models.DetailRestaurant;
import com.draft.draftlunch.Models.Nearby;
import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.Models.ResultDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RestaurantRepository {

    //  DATA

    private static volatile RestaurantRepository instance;
    private static UserRepository userRepository = UserRepository.getInstance();
    private static Disposable disposable;
    private static List<Result> myRestaurants = new ArrayList<>();
    public static ResultDetail detailRestaurant;


    // CONSTRUCTOR

    public RestaurantRepository() { }

    public static RestaurantRepository getInstance() {
        RestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantRepository();
            }
            return instance;
        }
    }

    // HTTP FOR TESTS

    public static OkHttpClient getHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return client;
    }

    // FETCH RESTAURANTS NEARBY

    public static void FetchRestaurants(String loc) {
        disposable = RestaurantRepository.streamFetchRestaurants(loc,4000,"restaurant","cruise","AIzaSyBQ4HmnvZGf8vwh-IvdUe8cCUsNENidYTo").subscribeWith(new DisposableObserver<Nearby>() {

            @Override
            public void onNext(Nearby restaurants) {
                setMyRestaurants(restaurants.getResults());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "ERROR !! -- " + e);
            }

            @Override
            public void onComplete() {
                //userRepository.CrossDataUsersAndRestaurant(getMyRestaurants());
            }
        });
    }

    public static Observable<Nearby> streamFetchRestaurants(String location, int radius, String type, String keyword, String key){
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        return retrofitService.getRestaurants(location,radius,type,keyword,key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // FETCH DETAILS RESTAURANT

    public static void FetchDetail(String place_id) {
        disposable = streamFetchDetail(place_id,"formatted_phone_number,website","AIzaSyBQ4HmnvZGf8vwh-IvdUe8cCUsNENidYTo").subscribeWith(new DisposableObserver<DetailRestaurant>() {

            @Override
            public void onNext(DetailRestaurant detailRestaurant) {
                setDetailRestaurant(detailRestaurant.getResultDetail());

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static Observable<DetailRestaurant> streamFetchDetail(String place_id, String fields, String key){
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        return retrofitService.getDetail(place_id,fields,key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // GETTER AND SETTER

    public static List<Result> getMyRestaurants() {return myRestaurants;}

    public static void setMyRestaurants(List<Result> myRestaurants) {
        RestaurantRepository.myRestaurants = myRestaurants;
    }

    public static ResultDetail getDetailRestaurant() {
        return detailRestaurant;
    }

    public static void setDetailRestaurant(ResultDetail detailRestaurant) {
        RestaurantRepository.detailRestaurant = detailRestaurant;
    }
}
