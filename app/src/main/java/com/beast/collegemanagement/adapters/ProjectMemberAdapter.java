package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ProjectMemberModel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectMemberAdapter extends RecyclerView.Adapter<ProjectMemberAdapter.MemberHolder> {

    List<ProjectMemberModel> list;
    Context context;

    String userName,
            profilePic,
            positionOfUser;

    public ProjectMemberAdapter(List<ProjectMemberModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProjectMemberAdapter.MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_stafflist_layout, parent, false);
        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectMemberAdapter.MemberHolder holder, int position) {

        userName = list.get(position).getUserName();
        profilePic = list.get(position).getProfilePic();
        positionOfUser = list.get(position).getPosition();

        Glide.with(context).load(profilePic).into(holder.profilePic);
        holder.userName.setText(userName);
        holder.position.setText(positionOfUser);

        holder.addBtn.setVisibility(View.VISIBLE);
        holder.addedBtn.setVisibility(View.GONE);

        Common.commonList.clear();

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.addBtn.setVisibility(View.GONE);
                holder.addedBtn.setVisibility(View.VISIBLE);

                if (!Common.commonList.contains(list.get(position).getUserName())){
                    Common.addToCommonList(list.get(position).getUserName());
                }

            }
        });

        holder.addedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.addedBtn.setVisibility(View.GONE);
                holder.addBtn.setVisibility(View.VISIBLE);

                Common.removeFromBoth(list.get(position).getUserName(), null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MemberHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;
        TextView userName, position;
        ImageView addBtn, addedBtn;

        public MemberHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePicStaff);
            userName = itemView.findViewById(R.id.userName);
            position = itemView.findViewById(R.id.position);
            addBtn = itemView.findViewById(R.id.addBtn);
            addedBtn = itemView.findViewById(R.id.addedBtn);

        }
    }
}
