package com.draft.draftlunch.ui.workmates;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
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
    private LiveData<List<User>> users;

    // CONSTRUCTOR

    public WorkmatesViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
    }

    public void init() {

        if (this.users != null) {
            return;
        }
        users = userSource.getUsers();
    }

    // -------------
    // FOR USER
    // -------------

    public LiveData<List<User>> getUsers() { return this.users; }

    // -------------
    // FOR RESTAURANT
    // -------------


}