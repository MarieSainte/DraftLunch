package com.draft.draftlunch.ui.main;


import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.draft.draftlunch.R;
import com.draft.draftlunch.databinding.ActivityMainBinding;
import com.draft.draftlunch.ui.ViewModelFactory;
import com.draft.draftlunch.ui.lunch.LunchActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final int RC_SIGN_IN = 123;
    private static final int ACCESS_LOCATION = 123;
    private ActivityMainBinding binding;
    private MainViewModel mViewModel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location = new Location("location");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        configureViewModel();

        //For Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getPermsLocation();

        // SET LISTENERS
        setupListeners();

    }

    //-----------------
    // SETUP THE VIEW MODEL
    //-----------------

    private void configureViewModel() {

        this.mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainViewModel.class);
        this.mViewModel.init();

    }

    //-----------------
    // SETUP LISTENERS
    //-----------------

    private void setupListeners(){
        //Login Button
        binding.btnConnexion.setOnClickListener(v -> startSignInActivity());

        binding.btnStart.setOnClickListener(v -> {
            startActivity(new Intent(this, LunchActivity.class));
        });
    }

    //-----------------
    // SIGN IN WITH FIREBASE
    //-----------------

    private void startSignInActivity(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers =
                Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.meal_v3_final)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                mViewModel.createUser();
            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError()!= null) {
                    if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }

    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    //-----------------
    // ASK FOR ACCESS TO THE LOCALISATION
    //-----------------

    @SuppressLint("MissingPermission")
    private void getPermsLocation() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {

                if (location != null){

                    setLocation(location.getLatitude(),location.getLongitude());

                }else{
                    Log.e("TAG", " Permission si granted BUT LOCATION = NULL");
                }
            });
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.request_perms),
                    ACCESS_LOCATION, perms);
        }
    }

    private void setLocation(double lat, double lng) {
        //location.setLatitude(lat);
        //location.setLongitude(lng);

        location.setLatitude(48.8650);
        location.setLongitude(2.3540);
        mViewModel.setLocation(location);
        Log.e(TAG, "setLocation with: " + location.getLatitude() );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {

            if (location != null){

                setLocation(location.getLatitude(),location.getLongitude());

            }else{
                Log.e("TAG", " Permission granted BUT LOCATION = NULL");

            }
        });
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

}