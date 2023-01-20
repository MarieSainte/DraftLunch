package com.draft.draftlunch.ui.details;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.ResultDetail;
import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class DetailsViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;


    // DATA
    final MutableLiveData<User> user = new MutableLiveData<>();
    List<User> JoiningUsers;
    String reservation;
    List likedRestaurant;
    // CONSTRUCTOR

    public DetailsViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
    }

    public void init(String place_id, String restaurant_name) {

        RestaurantRepository.FetchDetail(place_id);
        JoiningUsers = userSource.getJoiningUsers(restaurant_name);
    }

    // -------------
    // FOR USER
    // -------------

    // FETCH ALL USERS WHO HAVE RESERVED THE RESTAURANT
    public List<User> getJoiningUsers(){return this.JoiningUsers;}

    public MutableLiveData<User> getUser(){
        //user.setValue(userSource.getUserData().continueWith(task -> task.getResult().toObject(User.class)).getResult());
        user.setValue(userSource.getUserData().getResult().toObject(User.class));
        return this.user;
    }

    // ADD THIS RESTAURANT TO THE FAVORITE LIST IN FIRESTORE
    public void addRestaurantLiked(String restaurantLiked){
        userSource.addRestaurantLiked(restaurantLiked);
    }

    // SET THIS RESTAURANT AS RESERVED IN FIRESTORE
    public void addReservation(String reservation){
        userSource.addReservation(reservation);
    }

    // -------------
    // FOR RESTAURANT
    // -------------

    public ResultDetail getDetailRestaurant(){return RestaurantRepository.getDetailRestaurant();}

}
