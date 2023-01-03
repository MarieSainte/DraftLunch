package com.draft.draftlunch.ui.message;

import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;

import java.util.concurrent.Executor;

public class MessageViewModel extends ViewModel {
    // REPOSITORIES

    private final UserRepository userSource;

    private final RestaurantRepository restaurantSource;

    private final Executor executor;

    // DATA


    // CONSTRUCTOR

    public MessageViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
    }
}
