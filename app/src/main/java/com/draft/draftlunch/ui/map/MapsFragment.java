package com.draft.draftlunch.ui.map;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {


    private MapsViewModel mViewModel;
    private Marker marker;
    private FloatingActionButton btnPosition;
    private ProgressBar progressBar;
    private SupportMapFragment mapFragment;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        private LiveData<List<Result>> restaurants ;

        @Override
        public void onMapReady(GoogleMap googleMap) {
            restaurants = mViewModel.getRestaurants();
            for(int i =0 ; i < restaurants.getValue().size() ; i++){

                LatLng position = new LatLng(restaurants.getValue().get(i).getGeometry().getLocation().getLat(), restaurants.getValue().get(i).getGeometry().getLocation().getLng());
                if (restaurants.getValue().get(i).getHasBeenReservedBy().size() > 0){
                    marker = googleMap.addMarker(new MarkerOptions().position(position)
                            .title(restaurants.getValue().get(i).getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(1)));
                }else{
                    marker = googleMap.addMarker(new MarkerOptions().position(position)
                            .title(restaurants.getValue().get(i).getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(1)));
                }
                marker.setTag(restaurants.getValue().get(i).getPlaceId());

            }

            btnPosition.setOnClickListener(v -> moveCameraToMyPosition(googleMap));
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
        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        mViewModel.init();
        mViewModel.getRestaurants().observe(getViewLifecycleOwner(), this::updateView);
        btnPosition = view.findViewById(R.id.floating_action_button);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void updateView(List<Result> restaurants) {
        if (restaurants != null && !restaurants.isEmpty()){
            progressBar.setVisibility(View.GONE);
            /*
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }*/
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void moveCameraToMyPosition(GoogleMap googleMap) {

        LatLng myPosition = new LatLng(48.8650,2.3540);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,14));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        /*Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("placeID", marker.getTag().toString());
        startActivity(intent);*/
        return false;
    }
}