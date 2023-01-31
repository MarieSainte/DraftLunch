package com.draft.draftlunch.ui.workmates;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.Services.UserRepository;

import java.util.List;

public class WorkmatesViewModel extends ViewModel {


    // REPOSITORIES

    private final UserRepository userSource;

    // DATA


    // CONSTRUCTOR

    public WorkmatesViewModel(UserRepository userSource) {
        this.userSource = userSource;
    }

    // -------------
    // FOR USER
    // -------------

    public MutableLiveData<List<User>> getUsers() {
        return UserRepository.getLiveUsers();
    }


}