package com.draft.draftlunch.ui.lunch;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LunchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 123;
    private AppBarConfiguration mAppBarConfiguration;
    private LunchViewModel mViewModel;
    protected ImageView img_profil;
    private TextView tv_name;
    private TextView tv_email;

    /*
     * TODO: DELETE                     - Problem: RULES IN FIRESTORE
     * TODO: GET USER DATA              - Error: TASK IS NOT YET COMPLETED
     * TODO: CHECK CROSS DATA           - CHECK
     * TODO: AUTOCOMPLETE               - Problem: GOOGLE API Auth
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.draft.draftlunch.databinding.ActivityLunchBinding binding = ActivityLunchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find our Navigation view
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);

        tv_name = headerView.findViewById(R.id.tv_name);
        tv_email = headerView.findViewById(R.id.tv_email);
        img_profil = headerView.findViewById(R.id.img_profil);

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),getString(R.string.google_api_key));
        }

        // Set a Toolbar to replace the ActionBar.
        setSupportActionBar(binding.appBarLunch.toolbar);
        // Find our drawer view
        DrawerLayout drawer = binding.drawerLayout;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_map,R.id.nav_list ,R.id.nav_workmates, R.id.nav_main, R.id.nav_settings, R.id.btnLogout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_lunch);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(binding.appBarLunch.bottomNavViewMenu, navController);
        navigationView.setNavigationItemSelectedListener(this);

        configureViewModel();
        updateUIWithUserData();
    }

    private void configureViewModel() {
        this.mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(LunchViewModel.class);
        this.mViewModel.init();
    }

    //----------------
    // SETUP NAVIGATION
    //---------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lunch, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_search) {
            startAutocompleteActivity();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_lunch);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.btnLogout){
            mViewModel.signOut(this);
            finish();
        }

        return false;
    }

    //----------------
    // UPDATE VIEWS
    //---------------
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

    //----------------
    // AUTOCOMPLETE
    //---------------
    public void startAutocompleteActivity(){
        PlacesClient placesClient = Places.createClient(this);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                Arrays.asList(Place.Field.ID, Place.Field.NAME))
                .setLocationBias(RectangularBounds.newInstance(
                      new LatLng(48.8630,2.3320),
                      new LatLng(48.8650,2.3540)))
                .setCountry("FR")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = Autocomplete.getPlaceFromIntent(data);

            }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
                Status status = Autocomplete.getStatusFromIntent(data);
            }else if (resultCode == RESULT_CANCELED){
                Log.e(TAG, "onActivityResult: CANCELED " );
            }
        }
    }
}