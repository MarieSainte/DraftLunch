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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.draft.draftlunch.Models.Result;
import com.draft.draftlunch.R;
import com.draft.draftlunch.Services.RestaurantRepository;
import com.draft.draftlunch.ui.details.DetailsActivity;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {


    private Context context;
    private List<Result> restaurants;
    private RestaurantRepository restaurantRepository;
    private Location location = new Location("location");
    private Location destination = new Location("destination");
    private int rate;
    private String photoURL;

    public ListAdapter(Context context,List<Result> restaurants, Location location ) {
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

        Result restaurant = restaurants.get(position);
        String photoRef="";
        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("PlaceID", restaurant.getPlaceId());
            intent.putExtra("Name", restaurant.getName());
            intent.putExtra("Address", restaurant.getVicinity());
            intent.putExtra("Rate", restaurant.getRating());

            intent.putExtra("photo_URL", restaurant.getPhotos().get(0).getPhotoReference());
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

        }catch (Exception e){

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
        String metre = String.valueOf((int)location.distanceTo(destination));
        holder.tv_metre.setText(metre + " m");

        // SETTING OPENING
        if (restaurant.getOpeningHours().getOpenNow()){
            holder.tv_opening.setText(R.string.Open_now);
        }else if (! restaurant.getOpeningHours().getOpenNow()){
            holder.tv_opening.setText(R.string.Close);
        }else {
            holder.tv_opening.setVisibility(View.INVISIBLE);
        }

        // SETTING STARS FOR RATING
        if (restaurant.getRating() > 0.5){
            rate = 1;
            holder.img_rate_first.setVisibility(View.VISIBLE);
            if (restaurant.getRating() > 1.5){
                rate = 2;
                holder.img_rate_second.setVisibility(View.VISIBLE);
            }
                if (restaurant.getRating() > 2.5){
                    rate = 3;
                    holder.img_rate_third.setVisibility(View.VISIBLE);
                }
        }else{
            rate = 0;
            holder.img_rate_first.setVisibility(View.INVISIBLE);
            holder.img_rate_second.setVisibility(View.INVISIBLE);
            holder.img_rate_third.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView img_photo;
        ImageView img_person;
        ImageView img_rate_first;
        ImageView img_rate_second;
        ImageView img_rate_third;
        TextView tv_name;
        TextView tv_metre;
        TextView tv_type;
        TextView tv_address;
        TextView tv_joining;
        TextView tv_opening;
        ConstraintLayout item;

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
