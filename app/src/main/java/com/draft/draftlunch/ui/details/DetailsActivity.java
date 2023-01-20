package com.draft.draftlunch.ui.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.R;
import com.draft.draftlunch.databinding.ActivityDetailsBinding;
import com.draft.draftlunch.ui.ViewModelFactory;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private DetailsViewModel mViewModel;
    private String photoURL;
    protected boolean liked = false;
    protected boolean hasReserved = false;
    protected List<User> users;
    Result restaurant = new Result();

    //TODO: CHECK DETAILS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        restaurant = (Result)getIntent().getSerializableExtra("Restaurant");

        configureViewModel();

        setupListener();
        setupRecyclerView();
        mViewModel.getUser().observe(this, this::updateView);
    }

    private void updateView(User user) {

        binding.tvName.setText(restaurant.getName());
        binding.tvAddress.setText(restaurant.getVicinity());

//        if (user.getRestaurantLiked() != null){
//            for( int i = 0; i < user.getRestaurantLiked().size(); i++){
//
//                if (user.getRestaurantLiked().get(i).equals(restaurant.getName())){
//
//                    liked = true;
//                    binding.starToLike.setImageTintList(AppCompatResources.getColorStateList(this, R.color.yellow));
//                }
//            }
//        }
//        if (user.getReservation().equals(restaurant.getName())){
//            hasReserved = true;
//            binding.btnReserved.setImageTintList(AppCompatResources.getColorStateList(this, R.color.green));
//        }

        // SETUP THE RESTAURANT IMAGE
        String photoRef = restaurant.getPhotos().get(0).getPhotoReference();
        if (photoRef != null){
            photoURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" +
                    photoRef +
                    "&key=AIzaSyBQ4HmnvZGf8vwh-IvdUe8cCUsNENidYTo";
        }

        Glide.with(this)
                .load(photoURL)
                .error(R.drawable.img_drawers)
                .centerCrop()
                .into(binding.imgPhotoDetail);



        // SETUP STARS FOR RATING
        double rate = restaurant.getRating();
        if (rate > 0.5){

            binding.imgRateFirst.setVisibility(View.VISIBLE);
            if (rate > 1.5){

                binding.imgRateSecond.setVisibility(View.VISIBLE);
            }
            if (rate > 2.5){
                binding.imgRateThird.setVisibility(View.VISIBLE);
            }
        }else{
            binding.imgRateFirst.setVisibility(View.INVISIBLE);
            binding.imgRateSecond.setVisibility(View.INVISIBLE);
            binding.imgRateThird.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRecyclerView() {
        if(users !=null) {
            RecyclerView recyclerView = findViewById(R.id.recyclerview_detail);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            DetailsAdapter detailsAdapter = new DetailsAdapter(this, users);
            recyclerView.setAdapter(detailsAdapter);
            detailsAdapter.notifyDataSetChanged();
        }
    }

    private void configureViewModel() {

        this.mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(DetailsViewModel.class);
        this.mViewModel.init(restaurant.getPlaceId(),restaurant.getName());
        users = mViewModel.getJoiningUsers();
    }

    private void setupListener() {

        // CALL BUTTON LISTENER
        binding.btnCall.setOnClickListener(v -> {
            if(mViewModel.getDetailRestaurant().getFormattedPhoneNumber() !=null){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mViewModel.getDetailRestaurant().getFormattedPhoneNumber()));
                startActivity(intent);
            }
        });

        // LIKE BUTTON LISTENER
        binding.btnLike.setOnClickListener(v -> {
            if(liked){
                liked = false;
                binding.starToLike.setImageTintList(AppCompatResources.getColorStateList(this, R.color.gray));
            }else{
                liked = true;
                binding.starToLike.setImageTintList(AppCompatResources.getColorStateList(this, R.color.yellow));
                mViewModel.addRestaurantLiked(restaurant.getName());
            }
        });

        // WEBSITE BUTTON LISTENER
        binding.btnWebsite.setOnClickListener(v -> {
            if(mViewModel.getDetailRestaurant().getWebsite() !=null){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mViewModel.getDetailRestaurant().getWebsite()));
                startActivity(browserIntent);
            }
        });

        // RESERVATION BUTTON LISTENER
        binding.btnReserved.setOnClickListener(v -> {
            if(hasReserved){
                hasReserved = false;
                binding.btnReserved.setImageTintList(AppCompatResources.getColorStateList(this, R.color.gray));
            }else{
                hasReserved = true;
                binding.btnReserved.setImageTintList(AppCompatResources.getColorStateList(this, R.color.green));
                mViewModel.addReservation(getIntent().getStringExtra("Name"));
            }
        });
    }
}