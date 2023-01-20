package com.draft.draftlunch.ui.list;

import android.location.Location;

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

    // DATA


    // CONSTRUCTOR

    public ListViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
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

    public MutableLiveData<List<Result>> getRestaurants() {
        return RestaurantRepository.getMyRestaurants();
    }
}