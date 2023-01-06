package com.draft.draftlunch.ui;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.draft.draftlunch.Services.ChatRepository;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.draft.draftlunch.ui.details.DetailsViewModel;
import com.draft.draftlunch.ui.list.ListViewModel;
import com.draft.draftlunch.ui.lunch.LunchViewModel;
import com.draft.draftlunch.ui.main.MainViewModel;
import com.draft.draftlunch.ui.map.MapsViewModel;
import com.draft.draftlunch.ui.message.MessageViewModel;
import com.draft.draftlunch.ui.settings.SettingsViewModel;
import com.draft.draftlunch.ui.workmates.WorkmatesViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory{


    private final UserRepository userSource;
    private final RestaurantRepository restaurantSource;
    private final ChatRepository chatSource;
    private final Executor executor;
    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance(Context context) {

        if (factory == null) {

            synchronized (ViewModelFactory.class) {

                if (factory == null) {
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }

    public ViewModelFactory(Context context) {
        this.userSource = new UserRepository().getInstance();
        this.chatSource = new ChatRepository().getInstance();
        this.restaurantSource = new RestaurantRepository().getInstance();
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    @NotNull
    public <T extends ViewModel>  T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(userSource, restaurantSource, executor);
        }

        if (modelClass.isAssignableFrom(WorkmatesViewModel.class)) {
            return (T) new WorkmatesViewModel(userSource, restaurantSource, executor);
        }

        if (modelClass.isAssignableFrom(MapsViewModel.class)) {
            return (T) new MapsViewModel(userSource, restaurantSource, executor);
        }

        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(userSource, restaurantSource, executor);
        }

        if (modelClass.isAssignableFrom(LunchViewModel.class)) {
            return (T) new LunchViewModel(userSource, restaurantSource, executor);
        }

        if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
            return (T) new DetailsViewModel(userSource, restaurantSource, executor);
        }

        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(userSource, restaurantSource, executor);
        }

        if (modelClass.isAssignableFrom(MessageViewModel.class)) {
            return (T) new MessageViewModel(userSource, chatSource, executor);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
