package com.draft.draftlunch.ui.main;

import android.location.Location;

import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    // DATA


    // CONSTRUCTOR

    public MainViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
    }

    // -------------
    // FOR USER
    // -------------

    public void createUser(){userSource.createUser();}
    public void fetchUsers(){userSource.fetchUsers();}
    public void setLocation(Location location) {
        userSource.setLocation(location);
    }
    public Task<User> getUserData(){
        return userSource.getUserData().continueWith(task -> task.getResult().toObject(User.class)) ;
    }
    // -------------
    // FOR RESTAURANT
    // -------------

}
