package com.draft.draftlunch.ui.workmates;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class WorkmatesViewModel extends ViewModel {


    // REPOSITORIES

    private final UserRepository userSource;

    // DATA
    @Nullable
    private final MutableLiveData<List<User>> liveUsers = new MutableLiveData<List<User>>(){} ;

    // CONSTRUCTOR

    public WorkmatesViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
    }

    public void init() {

        if (this.liveUsers != null || !Objects.requireNonNull(Objects.requireNonNull(this.liveUsers).getValue()).isEmpty()) {
            return;
        }
        liveUsers.setValue(UserRepository.getLiveUsers().getValue());
    }

    // -------------
    // FOR USER
    // -------------

    public MutableLiveData<List<User>> getUsers() {
        Objects.requireNonNull(liveUsers).setValue(userSource.getUsers().getValue());
        return this.liveUsers;
    }

    // -------------
    // FOR RESTAURANT
    // -------------
}