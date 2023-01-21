package com.draft.draftlunch.ui.lunch;

import android.content.Context;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LunchViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    // DATA
    @Nullable
    private FirebaseUser user;

    // CONSTRUCTOR

    public LunchViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
    }

    public void init() {

        userSource.fetchUsers();
        user = userSource.getCurrentUser();
        userSource.fetchUserData();
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

    // Get the user from Firestore and cast it to a User model Object
    public Task<User> getUserData(){
        return userSource.getUserData().continueWith(task -> task.getResult().toObject(User.class)) ;
    }

}
