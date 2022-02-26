package com.example.jobproject2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobproject2.Interfaces.IUsersAdapterEvents;
import com.example.jobproject2.Models.User;
import com.example.jobproject2.R;
import com.example.jobproject2.Tools.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private ArrayList<User> usersArrayList;
    private Context ctx;
    private IUsersAdapterEvents events;

    public UsersAdapter(ArrayList<User> usersArrayList, Context ctx) {
        this.usersArrayList = usersArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public UsersAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item, parent, false);
        UsersViewHolder usersViewHolder = new UsersViewHolder(v);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersViewHolder holder, int position) {
        Utils.animateRecyclerViewItemFromLeftToRight(holder.itemView);

        User user = usersArrayList.get(position);

        holder.userNameTxtV.setText(user.getName());

        if(user.getDescription() != null && !user.getDescription().isEmpty())
            holder.userDescriptionTxtV.setText(user.getDescription());
        else
            holder.userDescriptionTxtV.setText("No description available");

        // load profile pictures
        Picasso.with(ctx).load(user.getProfilePicture()).placeholder(R.drawable.user_placeholder).into(holder.employeeCircleImageView);

        holder.itemView.setOnClickListener(view -> events.onItemClicked(user));

        holder.phoneImgBtn.setOnClickListener(view -> events.onPhoneImageButtonClicked(user));

        holder.emailImgBtn.setOnClickListener(view -> events.onEmailImageButtonClicked(user));

        holder.favImgBtn.setOnClickListener(view -> {
            events.onFavImageBtnClicked(user);
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public void setEvents(IUsersAdapterEvents events) {
        this.events = events;
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView employeeCircleImageView;
        private TextView userNameTxtV, userDescriptionTxtV;
        private ImageButton emailImgBtn, phoneImgBtn, favImgBtn;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            employeeCircleImageView = itemView.findViewById(R.id.employeeCircleImageView);
            userNameTxtV = itemView.findViewById(R.id.userNameTxtV);
            userDescriptionTxtV = itemView.findViewById(R.id.userDescriptionTxtV);
            emailImgBtn = itemView.findViewById(R.id.emailImgBtn);
            phoneImgBtn = itemView.findViewById(R.id.phoneImgBtn);
            favImgBtn = itemView.findViewById(R.id.favImgBtn);
        }
    }
}
