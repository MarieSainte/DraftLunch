package com.draft.draftlunch.ui.list;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.R;
import com.draft.draftlunch.ui.details.DetailsActivity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {


    protected final Context context;
    protected final MutableLiveData<List<Result>> restaurants;
    protected final Location location = new Location("location");
    protected final Location destination = new Location("destination");
    protected String photoURL;

    public ListAdapter(Context context, MutableLiveData<List<Result>> restaurants, Location location ) {
        this.context = context;
        this.restaurants = restaurants;

        location.setLatitude(location.getLatitude());
        location.setLongitude(location.getLongitude());
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        Result restaurant = Objects.requireNonNull(restaurants.getValue()).get(position);
        String photoRef="";
        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("Restaurant", (Serializable) restaurant);
            context.startActivity(intent);
        });

        try {
            photoRef = restaurant.getPhotos().get(0).getPhotoReference();
             photoURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" +
                     photoRef +
                    "&key=AIzaSyBQ4HmnvZGf8vwh-IvdUe8cCUsNENidYTo";
        }catch (Exception e){
            photoURL="";
        }
        try{
            if(!photoRef.isEmpty()){
                Glide.with(context)
                        .load(photoURL)
                        .centerCrop()
                        .into(holder.img_photo);
            }else{

                Glide.with(context)
                        .load(photoURL)
                        .error(R.drawable.img_drawers)
                        .centerCrop()
                        .into(holder.img_photo);
            }

        }catch (Exception ignored){

        }

        if(restaurant.getHasBeenReservedBy().size() > 0 ){
            holder.img_person.setVisibility(View.VISIBLE);
            holder.tv_joining.setVisibility(View.VISIBLE);
            holder.tv_joining.setText("("+restaurant.getHasBeenReservedBy().size()+")");
        }else{
            holder.img_person.setVisibility(View.INVISIBLE);
            holder.tv_joining.setVisibility(View.INVISIBLE);
        }


        holder.tv_name.setText(restaurant.getName());
        holder.tv_type.setVisibility(View.GONE);
        holder.tv_address.setText(restaurant.getVicinity());


        destination.setLatitude(restaurant.getGeometry().getLocation().getLat());
        destination.setLongitude(restaurant.getGeometry().getLocation().getLng());
        String metre = String.valueOf((int)location.distanceTo(destination)/1000);
        holder.tv_metre.setText(metre + " m");

        // SETTING OPENING
        if (restaurant.getOpeningHours().getOpenNow()){
            holder.tv_opening.setText(R.string.Open_now);
        }else if (!restaurant.getOpeningHours().getOpenNow()){
            holder.tv_opening.setText(R.string.Close);
        }else {
            holder.tv_opening.setVisibility(View.INVISIBLE);
        }

        // SETTING STARS FOR RATING
        if (restaurant.getRating() > 0.5){
            holder.img_rate_first.setVisibility(View.VISIBLE);
            if (restaurant.getRating() > 1.5){
                holder.img_rate_second.setVisibility(View.VISIBLE);
            }
                if (restaurant.getRating() > 2.5){
                    holder.img_rate_third.setVisibility(View.VISIBLE);
                }
        }else{
            holder.img_rate_first.setVisibility(View.INVISIBLE);
            holder.img_rate_second.setVisibility(View.INVISIBLE);
            holder.img_rate_third.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(restaurants.getValue()).size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{

        final ImageView img_photo;
        final ImageView img_person;
        final ImageView img_rate_first;
        final ImageView img_rate_second;
        final ImageView img_rate_third;
        final TextView tv_name;
        final TextView tv_metre;
        final TextView tv_type;
        final TextView tv_address;
        final TextView tv_joining;
        final TextView tv_opening;
        final ConstraintLayout item;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_list);
            img_photo = itemView.findViewById(R.id.img_photo);
            img_person = itemView.findViewById(R.id.img_person);
            img_rate_first = itemView.findViewById(R.id.img_rate_first);
            img_rate_second = itemView.findViewById(R.id.img_rate_second);
            img_rate_third = itemView.findViewById(R.id.img_rate_third);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_metre = itemView.findViewById(R.id.tv_metre);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_joining = itemView.findViewById(R.id.tv_joining);
            tv_opening = itemView.findViewById(R.id.tv_opening);
        }
    }
}
