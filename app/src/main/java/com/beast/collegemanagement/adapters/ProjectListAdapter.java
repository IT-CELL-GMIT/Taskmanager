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

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.ProjectActivity;
import com.beast.collegemanagement.ProjectTasksActivity;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ProjectListModel;
import com.beast.collegemanagement.tabfragment.AddTaskFragment;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectHolder> {

    List<ProjectListModel> list;
    Context context;
    String activityName;

    public ProjectListAdapter(List<ProjectListModel> list, Context context, String activityName) {
        this.list = list;
        this.context = context;
        this.activityName = activityName;
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

        if (list.get(position).getUniqueID().equalsIgnoreCase("none")) {

            holder.title.setText("Remove from project");
            holder.userProfileLL.setVisibility(View.GONE);

        }else {


            if (projectStatus.equalsIgnoreCase("RUNNING")) {

                holder.profileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.project_icon));
                holder.status.setVisibility(View.GONE);
                holder.completeLL.setVisibility(View.GONE);

            } else {

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

        }


        if (activityName.equalsIgnoreCase("AddProjectFragment")) {
            holder.fullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.checkBtn.setVisibility(View.GONE);

                    context.startActivity(new Intent(context, ProjectTasksActivity.class)
                            .putExtra("title", list.get(position).getTitle())
                            .putExtra("dateTime", list.get(position).getDateTime())
                            .putExtra("leaderProfileImage", list.get(position).getLeaderProfileImage())
                            .putExtra("leaderUsername", list.get(position).getLeaderUsername())
                            .putExtra("projectStatus", list.get(position).getProjectStatus())
                            .putExtra("closeDate", list.get(position).getClosedDate())
                            .putExtra("project_id", list.get(position).getUniqueID()));

                }
            });
        }else if (activityName.equalsIgnoreCase("ProjectFragment")){

            holder.lastTextLL.setVisibility(View.GONE);

            holder.fullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.checkBtn.getVisibility() == View.VISIBLE){

                        holder.checkBtn.setVisibility(View.GONE);
                        Common.commonList.remove(list.get(position).getUniqueID());

                    }else {

                        holder.checkBtn.setVisibility(View.VISIBLE);
                        Common.commonList.add(list.get(position).getUniqueID());

                    }


                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProjectHolder extends RecyclerView.ViewHolder {

        TextView title, leaderName, time, date, status, completeDate;
        ImageView profileImg, checkBtn;
        CircleImageView leaderProfileImg;
        LinearLayout fullLayout, completeLL, lastTextLL, userProfileLL;

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
            lastTextLL = itemView.findViewById(R.id.lastTextLL);
            checkBtn = itemView.findViewById(R.id.checkBtn);
            userProfileLL = itemView.findViewById(R.id.userProfileLL);

        }
    }
}
