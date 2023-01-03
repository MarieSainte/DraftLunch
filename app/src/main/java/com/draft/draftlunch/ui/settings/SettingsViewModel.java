package com.draft.draftlunch.ui.settings;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;

import java.util.concurrent.Executor;

public class SettingsViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    private final RestaurantRepository restaurantSource;

    private final Executor executor;

    // DATA


    // CONSTRUCTOR

    public SettingsViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
        this.restaurantSource = restaurantSource;
        this.executor = executor;
    }


    public void deleteUser(Context context) {
        userSource.deleteUser(context);
    }
}
