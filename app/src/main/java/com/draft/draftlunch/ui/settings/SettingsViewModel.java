package com.draft.draftlunch.ui.settings;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

public class SettingsViewModel extends ViewModel {

    // REPOSITORIES

    private final UserRepository userSource;

    // DATA


    // CONSTRUCTOR

    public SettingsViewModel(UserRepository userSource, RestaurantRepository restaurantSource, Executor executor) {
        this.userSource = userSource;
    }
    public void deleteUser(){
        userSource.deleteUser();
    }
    public Task<Void> deleteUser(Context context){
        return userSource.deleteUser(context).addOnCompleteListener(task -> userSource.deleteUserFromFirestore());
    }
}
