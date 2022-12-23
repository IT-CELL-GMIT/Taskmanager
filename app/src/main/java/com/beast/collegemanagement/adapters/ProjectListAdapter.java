package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.ProjectActivity;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ProjectListModel;
import com.beast.collegemanagement.tabfragment.AddTaskFragment;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectHolder> {

    List<ProjectListModel> list;
    Context context;

    public ProjectListAdapter(List<ProjectListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_project_list, parent, false);

        return new ProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {

        String title,
                dateTime,
                leaderProfileImage,
                leaderUsername,
        projectStatus,
        closeDate;

        title = list.get(position).getTitle();
        dateTime = list.get(position).getDateTime();
        leaderProfileImage = list.get(position).getLeaderProfileImage();
        leaderUsername = list.get(position).getLeaderUsername();
        projectStatus = list.get(position).getProjectStatus();
        closeDate = list.get(position).getClosedDate();

        if (projectStatus.equalsIgnoreCase("RUNNING")){

            holder.profileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.project_icon));
            holder.status.setVisibility(View.GONE);
            holder.completeLL.setVisibility(View.GONE);

        }else {

            holder.status.setVisibility(View.VISIBLE);
            holder.profileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.project_closed));
            holder.completeLL.setVisibility(View.VISIBLE);
            String comSplit[] = closeDate.split("xxx");
            holder.completeDate.setText(comSplit[0] + " - " + comSplit[1]);

        }

        holder.leaderName.setText(leaderUsername);
        Glide.with(context).load(leaderProfileImage).into(holder.leaderProfileImg);
        holder.title.setText(title);

        String splits[] = dateTime.split("xxx");
        holder.date.setText(splits[0]);
        holder.time.setText(splits[1]);

        holder.fullLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProjectHolder extends RecyclerView.ViewHolder {

        TextView title, leaderName, time, date, status, completeDate;
        ImageView profileImg;
        CircleImageView leaderProfileImg;
        LinearLayout fullLayout, completeLL;

        public ProjectHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            leaderName = itemView.findViewById(R.id.tv_leaderName);
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
            profileImg = itemView.findViewById(R.id.profileImg);
            status = itemView.findViewById(R.id.tv_status);
            leaderProfileImg = itemView.findViewById(R.id.leaderProfileImg);
            fullLayout= itemView.findViewById(R.id.fullLayout);
            completeLL = itemView.findViewById(R.id.completeLL);
            completeDate = itemView.findViewById(R.id.tv_closeDate);

        }
    }
}
