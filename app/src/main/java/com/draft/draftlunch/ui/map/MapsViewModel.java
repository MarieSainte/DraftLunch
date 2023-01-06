package com.draft.draftlunch.ui.map;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class MapsViewModel extends ViewModel {


    // REPOSITORIES

    private final UserRepository userSource;

    private final RestaurantRepository restaurantSource;

    private final Executor executor;

    // DATA
    @Nullable
    private MutableLiveData<List<Result>> restaurants ;

    // CONSTRUCTOR

    public MapsViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
        restaurants = new MutableLiveData<List<Result>>() {} ;
    }

    public void init() {

        if (this.restaurants != null) {
            return;
        }
        restaurants.setValue(restaurantSource.getMyRestaurants());
    }

    // -------------
    // FOR USER
    // -------------


    // -------------
    // FOR RESTAURANT
    // -------------
    public MutableLiveData<List<Result>> getRestaurants() {
        if (this.restaurants != null){
            return this.restaurants;
        }else if (userSource.getAllRestaurants() != null){
            restaurants.setValue(restaurantSource.getMyRestaurants());
            return restaurants;
        }
        return this.restaurants;
    }
}