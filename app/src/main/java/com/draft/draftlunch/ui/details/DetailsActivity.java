package com.draft.draftlunch.ui.details;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.draft.draftlunch.Models.ResultDetail;
import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.R;
import com.draft.draftlunch.databinding.ActivityDetailsBinding;
import com.draft.draftlunch.ui.ViewModelFactory;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private DetailsViewModel mViewModel;
    private LiveData<List<User>> users;
    private RecyclerView recyclerView;
    private ResultDetail resultDetail;
    private String photoURL;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureViewModel();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        binding.tvName.setText(getIntent().getStringExtra("Name"));
        binding.tvAddress.setText(getIntent().getStringExtra("Address"));


        String photoRef = getIntent().getStringExtra("photo_URL");
        try {
            photoURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" +
                    photoRef +
                    "&key=AIzaSyBQ4HmnvZGf8vwh-IvdUe8cCUsNENidYTo";
        }catch(Exception e){

        }

        if(!getIntent().getStringExtra("photo_URL").isEmpty()){
            Glide.with(this)
                    .load(photoURL)
                    .error(R.drawable.img_drawers)
                    .centerCrop()
                    .into(binding.imgPhotoDetail);
        }
        double rate = getIntent().getDoubleExtra("Rate",0.0);
       // SETTING STARS FOR RATING
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

        users = mViewModel.getJoiningUsers(getIntent().getStringExtra("Name"));
        setupListener();

        if(users!=null || users.getValue().size()!=0){
            recyclerView = findViewById(R.id.recyclerview_detail);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            DetailsAdapter detailsAdapter = new DetailsAdapter(this, users.getValue());
            recyclerView.setAdapter(detailsAdapter);
            detailsAdapter.notifyDataSetChanged();
        }
    }

    private void configureViewModel() {

        this.mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(DetailsViewModel.class);

        this.mViewModel.init(getIntent().getStringExtra("PlaceID"),getIntent().getStringExtra("Name"));

    }

    private void setupListener() {

        String number = mViewModel.getDetailRestaurant().getFormattedPhoneNumber();
        String website = mViewModel.getDetailRestaurant().getWebsite();

        binding.btnCall.setOnClickListener(v -> {

            if(number !=null || !number.isEmpty()){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                startActivity(intent);
            }else{
                Log.e(TAG, "Pas de Numero");
            }

        });

        binding.btnLike.setOnClickListener(v -> {

            binding.starToLike.setImageTintList(AppCompatResources.getColorStateList(this, R.color.yellow));
            mViewModel.addRestaurantLiked(getIntent().getStringExtra("Name"));
        });

        binding.btnWebsite.setOnClickListener(v -> {
            if(website !=null || !website.isEmpty()){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mViewModel.getDetailRestaurant().getWebsite()));
                startActivity(browserIntent);
            }

        });

        binding.btnReserved.setOnClickListener(v -> {
            binding.btnReserved.setImageTintList(AppCompatResources.getColorStateList(this, R.color.green));
            mViewModel.addReservation(getIntent().getStringExtra("Name"));
            mFirebaseAnalytics.setUserProperty("hasReserved", getIntent().getStringExtra("Name"));
        });
    }

    public ResultDetail getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(ResultDetail resultDetail) {
        this.resultDetail = resultDetail;
    }
}