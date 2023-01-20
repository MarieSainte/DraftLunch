package com.draft.draftlunch.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.draft.draftlunch.R;
import com.draft.draftlunch.Services.LangManager;
import com.draft.draftlunch.databinding.ActivitySettingsBinding;
import com.draft.draftlunch.ui.ViewModelFactory;
import com.draft.draftlunch.ui.main.MainActivity;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivitySettingsBinding binding ;
    private SettingsViewModel mViewModel;
    private final LangManager langManager = new LangManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureViewModel();
        setupListeners();
        setupSpinner();
    }

    private void configureViewModel() {

        this.mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(SettingsViewModel.class);

    }

    private void setupListeners() {
        binding.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, (dialogInterface, i) ->
                        mViewModel.deleteUser(this)
                                .addOnSuccessListener(aVoid -> {
                                    Intent intent = new Intent(this, MainActivity.class);
                                    startActivity(intent);
                                        }
                                )
                )
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show());

        binding.spinnerLanguages.setOnItemSelectedListener(this);

        binding.backArrow.setOnClickListener(v-> onBackPressed());
    }

    private void setupSpinner(){
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this,R.array.languages,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLanguages.setAdapter(spinner_adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {

            langManager.setLang("en");
        }else if (position == 1) {

            langManager.setLang("fr");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}