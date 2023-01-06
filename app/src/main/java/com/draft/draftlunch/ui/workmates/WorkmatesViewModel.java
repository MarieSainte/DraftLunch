package com.draft.draftlunch.ui.workmates;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class WorkmatesViewModel extends ViewModel {


    // REPOSITORIES

    private final UserRepository userSource;

    private final RestaurantRepository restaurantSource;

    private final Executor executor;

    // DATA
    @Nullable
    private MutableLiveData<List<User>> liveUsers;

    // CONSTRUCTOR

    public WorkmatesViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
        liveUsers = new MutableLiveData<List<User>>(){} ;
    }

    public void init() {

        if (this.liveUsers != null || !this.liveUsers.getValue().isEmpty()) {
            return;
        }
        liveUsers.setValue(userSource.getLiveUsers().getValue());
    }

    // -------------
    // FOR USER
    // -------------

    public MutableLiveData<List<User>> getUsers() { return this.liveUsers; }

    // -------------
    // FOR RESTAURANT
    // -------------


}