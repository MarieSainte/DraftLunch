package com.draft.draftlunch.ui.lunch;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.draft.draftlunch.R;
import com.draft.draftlunch.databinding.ActivityLunchBinding;
import com.draft.draftlunch.ui.ViewModelFactory;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

public class LunchActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLunchBinding binding;
    private LunchViewModel mViewModel;
    private ImageView img_profil;
    private TextView tv_name;
    private TextView tv_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLunchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tv_name = (TextView) headerView.findViewById(R.id.tv_name);
        tv_email = (TextView) headerView.findViewById(R.id.tv_email);
        img_profil = (ImageView) headerView.findViewById(R.id.img_profil);
        View search_bar = findViewById(R.id.search_bar);
        setSupportActionBar(binding.appBarLunch.toolbar);

        configureViewModel();

        DrawerLayout drawer = binding.drawerLayout;
        updateUIWithUserData();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_map,R.id.nav_list ,R.id.nav_workmates, R.id.nav_main, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_lunch);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(binding.appBarLunch.bottomNavViewMenu, navController);
/*
        // AUTOCOMPLETE

        String apiKey = getString(R.string.google_api_key);
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),apiKey);
        }
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autoComplete);

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(48.8650,2.3540),
                new LatLng(48.8650,2.3540)
        ));

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });*/

    }

    private void configureViewModel() {

        this.mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(LunchViewModel.class);
        this.mViewModel.init();
        mViewModel.getUsers().observe(this ,mViewModel::fetchRestaurants);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lunch, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_lunch);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void updateUIWithUserData(){

        if(mViewModel.getCurrentUser() != null){
            FirebaseUser user = mViewModel.getCurrentUser();
            try {
                tv_name.setText(user.getDisplayName());
                tv_email.setText(user.getEmail());
            }catch (Exception e){
                Log.e(TAG, "updateUIWithUserData: ", e);
            }

            if(user.getPhotoUrl() != null){
                setProfilePicture(user.getPhotoUrl());
            }

        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(img_profil);
    }
}