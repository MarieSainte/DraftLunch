package com.draft.draftlunch.ui.list;

import android.location.Location;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class ListViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    private final RestaurantRepository restaurantSource;

    private final Executor executor;

    // DATA
    @Nullable
    private MutableLiveData<List<Result>> restaurants ;

    // CONSTRUCTOR

    public ListViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
        restaurants = new MutableLiveData<List<Result>>(){} ;
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

    public Location getLocation() {
        return userSource.getLocation();
    }

    // -------------
    // FOR RESTAURANT
    // -------------

    public MutableLiveData<List<Result>> getRestaurants() { return this.restaurants; }


}