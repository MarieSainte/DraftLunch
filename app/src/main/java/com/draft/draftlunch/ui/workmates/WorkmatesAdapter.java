package com.draft.draftlunch.ui.workmates;

import android.content.Context;
import android.content.Intent;
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
import com.draft.draftlunch.ui.message.MessageActivity;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.WorkmatesViewHolder>{

    Context context;
    List<User> users;

    public WorkmatesAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_people,parent,false);
        return new WorkmatesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesAdapter.WorkmatesViewHolder holder, int position) {

        User user = users.get(position);

        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("urlPicture", user.getUrlPicture());
            intent.putExtra("Name", user.getUsername());
            context.startActivity(intent);
        });

        Glide.with(context)
                .load(user.getUrlPicture())
                .error(R.drawable.ic_baseline_person_24)
                .circleCrop()
                .into(holder.img_photo);



        if(user.getReservation()!=null){
            holder.tv_status.setText(user.getUsername()+" is eating at " + user.getReservation());
        }else{
            holder.tv_status.setText(user.getUsername()+" hasn't decided yet");
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

        ImageView img_photo;
        TextView tv_status;
        ConstraintLayout item;

        public WorkmatesViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item_workmates);
            img_photo = itemView.findViewById(R.id.img_photo);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
