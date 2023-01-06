package com.draft.draftlunch.ui.message;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.draft.draftlunch.R;
import com.draft.draftlunch.ui.ViewModelFactory;

public class MessageActivity extends AppCompatActivity {

    private MessageViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        configureViewModel();
    }

    private void configureViewModel() {

        this.mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MessageViewModel.class);

        this.mViewModel.init();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}