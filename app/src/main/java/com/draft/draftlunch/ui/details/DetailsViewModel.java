package com.draft.draftlunch.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.ResultDetail;
import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.Executor;

public class DetailsViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    private final RestaurantRepository restaurantSource;

    private final Executor executor;

    // DATA

    LiveData<List<User>> JoiningUsers;

    // CONSTRUCTOR

    public DetailsViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
    }

    public void init(String place_id, String restaurant_name) {

        restaurantSource.FetchDetail(place_id);
        JoiningUsers = userSource.getJoiningUsers(restaurant_name);
    }

    // -------------
    // FOR USER
    // -------------

    // FETCH ALL USERS WHO HAVE RESERVED THE RESTAURANT
    public LiveData<List<User>> getJoiningUsers(String name){return this.JoiningUsers;}

    public Task<Void> addRestaurantLiked(String restaurantLiked){
        return userSource.addRestaurantLiked(restaurantLiked);
    }

    public Task<Void> addReservation(String reservation){
        return userSource.addReservation(reservation);
    }

    // -------------
    // FOR RESTAURANT
    // -------------

    public ResultDetail getDetailRestaurant(){return restaurantSource.getDetailRestaurant();}


}
