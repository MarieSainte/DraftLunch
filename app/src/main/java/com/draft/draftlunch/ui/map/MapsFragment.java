package com.draft.draftlunch.ui.map;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.R;
import com.draft.draftlunch.databinding.FragmentMapsBinding;
import com.draft.draftlunch.ui.ViewModelFactory;
import com.draft.draftlunch.ui.details.DetailsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private FragmentMapsBinding binding;
    private MapsViewModel mViewModel;
    private SupportMapFragment mapFragment;
    protected MutableLiveData<List<Result>> restaurants ;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            restaurants = mViewModel.getRestaurants();

            // SET ALL MARKERS
            for(int i = 0; i < Objects.requireNonNull(restaurants.getValue()).size() ; i++){

                LatLng position = new LatLng(restaurants.getValue().get(i).getGeometry().getLocation().getLat(), restaurants.getValue().get(i).getGeometry().getLocation().getLng());
                Marker marker;
                if (restaurants.getValue().get(i).getHasBeenReservedBy().size() > 0){
                    marker = googleMap.addMarker(new MarkerOptions().position(position)
                            .title(restaurants.getValue().get(i).getName())
                            .icon(getMarkerIcon("#22ad1f")));
                    Objects.requireNonNull(marker).setTag(i);
                }else{
                    marker = googleMap.addMarker(new MarkerOptions().position(position)
                            .title(restaurants.getValue().get(i).getName()));
                    Objects.requireNonNull(marker).setTag(i);
                }


            }

            // SET LISTENER FOR MyPOSITION BUTTON
            binding.floatingActionButton.setOnClickListener(v -> moveCameraToMyPosition(googleMap));
            moveCameraToMyPosition(googleMap);

            // Set a listener for marker click.
            googleMap.setOnMarkerClickListener(MapsFragment.this);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getContext())).get(MapsViewModel.class);

        mViewModel.getRestaurants().observe(getViewLifecycleOwner(), this::updateView);
    }

    private void updateView(List<Result> restaurants) {

        if (restaurants != null){
            binding.progressBar.setVisibility(View.GONE);

            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private void moveCameraToMyPosition(GoogleMap googleMap) {

        LatLng myPosition = new LatLng(mViewModel.getLocation().getLatitude(),mViewModel.getLocation().getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,14));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Log.e(TAG, "onMarkerClick: "+ marker.getTag() );
        if (marker.getTag() != null){
            startDetailsActivity(Objects.requireNonNull(mViewModel.getRestaurants().getValue()).get((int)marker.getTag()));
        }
        return true;
    }

    public void startDetailsActivity(Result restaurant){
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("Restaurant", (Serializable) restaurant);
        startActivity(intent);
    }
}