package com.draft.draftlunch.ui.message;

import androidx.lifecycle.ViewModel;

import com.draft.draftlunch.Services.ChatRepository;
import com.draft.draftlunch.Services.UserRepository;
import com.google.firebase.firestore.Query;

import java.util.concurrent.Executor;

public class MessageViewModel extends ViewModel {
    // REPOSITORIES

    private final UserRepository userSource;

    private final ChatRepository chatSource;

    private final Executor executor;

    // DATA


    // CONSTRUCTOR

    public MessageViewModel(UserRepository userSource, ChatRepository chatSource, Executor executor) {
        this.userSource = userSource;
        this.chatSource = chatSource;
        this.executor = executor;
    }

    public Query getAllMessageForChat(String chat){
        return chatSource.getAllMessageForChat(chat);
    }

    public void init() {

    }
}
