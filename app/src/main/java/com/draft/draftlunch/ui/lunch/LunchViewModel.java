package com.draft.draftlunch.ui.lunch;

import android.content.Context;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class LunchViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    // DATA
    @Nullable
    private FirebaseUser user;

    // CONSTRUCTOR

    public LunchViewModel(UserRepository userSource) {
        this.userSource = userSource;
    }

    public void init() {

        userSource.fetchUsers();
        userSource.fetchUserData();
        user = userSource.getCurrentUser();

    }

    // -------------
    // FOR USER
    // -------------

    public Task<Void> signOut(Context context){
        return userSource.signOut(context);
    }
    public Location getLocation() {
        return userSource.getLocation();
    }

    public FirebaseUser getCurrentUser() { return this.user; }


    public Result getSpecificRestaurant(String name) {
        return RestaurantRepository.getSpecificRestaurant(name);
    }
}
