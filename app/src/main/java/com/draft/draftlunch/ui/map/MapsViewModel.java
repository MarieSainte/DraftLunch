package com.draft.draftlunch.ui.map;

import android.location.Location;

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

    // CONSTRUCTOR

    public MapsViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;

    }

    // -------------
    // FOR USER
    // -------------


    // -------------
    // FOR RESTAURANT
    // -------------
    public MutableLiveData<List<Result>> getRestaurants() {
        return RestaurantRepository.getMyRestaurants();
    }


    public List<Result> CrossDataUsersAndRestaurant(MutableLiveData<List<Result>> restaurants) {
        return  userSource.CrossDataUsersAndRestaurant(restaurants);
    }

    public Location getLocation() {
        return userSource.getLocation();
    }
}