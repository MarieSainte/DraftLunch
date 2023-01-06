package com.draft.draftlunch.ui.lunch;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Executor;

public class LunchViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    private final RestaurantRepository restaurantSource;

    private final Executor executor;

    // DATA
    @Nullable
    private FirebaseUser user;

    // CONSTRUCTOR

    public LunchViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
    }

    public void init() {
        fetchUsers();
        restaurantSource.FetchRestaurants("48.8650,2.3540");
        if (this.user != null) {
            return;
        }
        user = userSource.getCurrentUser();

    }

    // -------------
    // FOR USER
    // -------------

    public void fetchUsers(){userSource.fetchUsers();}

    public FirebaseUser getCurrentUser() { return this.user; }

    public Task<Void> signOut(Context context){
        return userSource.signOut(context);
    }

    public Task<Void> deleteUser(Context context){
        // Delete the user account from the Auth
        return userSource.deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user datas from Firestore
            userSource.deleteUserFromFirestore();
        });
    }

    // Get the user from Firestore and cast it to a User model Object
    public Task<User> getUserData(){
        return userSource.getUserData().continueWith(task -> task.getResult().toObject(User.class)) ;
    }

    public LiveData<List<User>> getUsers() {
        return userSource.getUsers();
    }


    // -------------
    // FOR RESTAURANT
    // -------------

    public void fetchRestaurants(List<User> users){
        if (users != null && restaurantSource.getMyRestaurants().isEmpty()){
            restaurantSource.FetchRestaurants("48.8650,2.3540");
        }
    }

}
