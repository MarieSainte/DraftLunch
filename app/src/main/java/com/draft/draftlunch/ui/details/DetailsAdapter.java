package com.draft.draftlunch.ui.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.draft.draftlunch.Models.User;
import com.draft.draftlunch.R;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailViewHolder>{

    Context context;
    List<User> users;

    public DetailsAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public DetailsAdapter.DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_people,parent,false);
        return new DetailsAdapter.DetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.DetailViewHolder holder, int position) {

        User user = users.get(position);

        Glide.with(context)
                .load(user.getUrlPicture())
                .error(R.drawable.ic_anon_user_48dp)
                .circleCrop()
                .into(holder.img_photo);

        holder.tv_status.setText(user.getUsername()+" is joining!");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {

        ImageView img_photo;
        TextView tv_status;
        ConstraintLayout item;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item_workmates);
            img_photo = itemView.findViewById(R.id.img_photo);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
