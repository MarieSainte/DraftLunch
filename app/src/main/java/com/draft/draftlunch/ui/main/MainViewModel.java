package com.draft.draftlunch.ui.main;

import android.location.Location;

import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;

import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    private final RestaurantRepository restaurantSource;

    private final Executor executor;

    // DATA


    // CONSTRUCTOR

    public MainViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
    }

    public void init() {


    }

    // -------------
    // FOR USER
    // -------------

    public void createUser(){userSource.createUser();}
    public void fetchUsers(){userSource.fetchUsers();}
    public void setLocation(Location location) {
        userSource.setLocation(location);
    }
    // -------------
    // FOR RESTAURANT
    // -------------

}
