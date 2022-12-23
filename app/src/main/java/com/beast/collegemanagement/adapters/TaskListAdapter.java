package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.ProjectActivity;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.TaskListModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {

    List<TaskListModel> list;
    Context context;

    String uniqueID, title,
            leaderName,
            leaderProfileImg,
            coLeaderProfileImg,
            time,
            date,
            status,
    completeDate;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public TaskListAdapter(List<TaskListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskListAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tasklist_layout, parent, false);

        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.TaskHolder holder, int position) {

        uniqueID = list.get(position).getUniqueID();
        title = list.get(position).getTitle();
        leaderName = list.get(position).getLeaderName();
        leaderProfileImg = list.get(position).getLeaderProfileImg();
        coLeaderProfileImg = list.get(position).getCoLeaderProfileImg();
        time = list.get(position).getTime();
        status = list.get(position).getStatus();
        date = list.get(position).getDate();
        completeDate = list.get(position).getCompleteDate();

        sp = context.getSharedPreferences("FILE_NAME", Context.MODE_PRIVATE);
        editor = sp.edit();

        String currentDate = new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        ////55-55_5555xxx55:55

        holder.leaderName.setText(leaderName);
        holder.title.setText(title);
        Glide.with(context).load(leaderProfileImg).into(holder.leaderProfile);
//        Glide.with(context).load(coLeaderProfileImg).into(holder.coProfile);



        if (status.equalsIgnoreCase("COMPLETED")){

            holder.imageProfile.setImageDrawable(context.getResources().getDrawable(R.drawable.task_complete));

            holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.status.setText("Completed");
            holder.status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.status.setVisibility(View.VISIBLE);

            holder.timeOutStatus.setVisibility(View.GONE);
            holder.taskCompleteStaus.setVisibility(View.GONE);

            if (currentDate.equalsIgnoreCase(completeDate)){
                holder.timeLeft.setText("today");
            }else {

                String cdSplit[] = currentDate.split("-");
                String cmDSplit [] = completeDate.split("-");

                if (cdSplit[1].equalsIgnoreCase(cmDSplit[1])){

                    int dayToday = Integer.parseInt(cdSplit[0]),
                            dayDone = Integer.parseInt(cmDSplit[0]);

                    int dayGape = dayToday - dayDone;

                    if (dayGape < 7){
                        holder.timeLeft.setText(String.valueOf(dayGape) + " days ago");
                    }else if (dayGape < 14){
                        holder.timeLeft.setText("1 week ago");
                    }else if (dayGape < 21){
                        holder.timeLeft.setText("2 weeks ago");
                    }else if (dayGape < 28){
                        holder.timeLeft.setText("3 weeks ago");
                    }else {
                        holder.timeLeft.setText("1 month ago");
                    }

                }else {
                    holder.timeLeft.setText("long ago");
                }

            }

        }else {

            holder.taskCompleteStaus.setVisibility(View.GONE);

            holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));

            String cdSplitDay[] = currentDate.split("-");
            String cdSplitYear[] = cdSplitDay[1].split("_");
            String dSplitDay[] = date.split("-");
            String dSplitYear[] = dSplitDay[1].split("_");

            int todayYear = Integer.parseInt(cdSplitYear[1]);
            int taskYear = Integer.parseInt(dSplitYear[1]);
            int todayMonth = Integer.parseInt(cdSplitYear[0]);
            int taskMonth = Integer.parseInt(dSplitYear[0]);
            int today = Integer.parseInt(cdSplitDay[0]);
            int task = Integer.parseInt(dSplitDay[0]);

            if (todayYear > taskYear){

                holder.imageProfile.setImageDrawable(context.getDrawable(R.drawable.task_timeover));

                holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
                holder.timeLeft.setText("time has over " + date);

                holder.status.setVisibility(View.GONE);
                holder.timeOutStatus.setVisibility(View.VISIBLE);

            }else if (todayYear < taskYear){

                holder.imageProfile.setImageDrawable(context.getDrawable(R.drawable.task_timer));

                holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.darkGrey));
                holder.timeLeft.setText(date);

                holder.status.setVisibility(View.VISIBLE);
                holder.timeOutStatus.setVisibility(View.GONE);

            }else{

                if (todayMonth > taskMonth){

                    holder.imageProfile.setImageDrawable(context.getDrawable(R.drawable.task_timeover));

                    holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
                    holder.timeLeft.setText("time has over " + date);

                    holder.status.setVisibility(View.GONE);
                    holder.timeOutStatus.setVisibility(View.VISIBLE);

                }else if (todayMonth < taskMonth){

                    holder.imageProfile.setImageDrawable(context.getDrawable(R.drawable.task_timer));

                    holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.darkGrey));
                    holder.timeLeft.setText(date);

                    holder.status.setVisibility(View.VISIBLE);
                    holder.timeOutStatus.setVisibility(View.GONE);

                }else {

                    int dayGape = task - today;

                    if (dayGape > 0){

                        holder.imageProfile.setImageDrawable(context.getDrawable(R.drawable.task_icon));

                        holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.darkGrey));
                        holder.timeLeft.setText(String.valueOf(dayGape + " days has left"));

                        holder.status.setVisibility(View.VISIBLE);
                        holder.timeOutStatus.setVisibility(View.GONE);

                    }else if (dayGape < 0){

                        holder.imageProfile.setImageDrawable(context.getDrawable(R.drawable.task_timeover));

                        holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
                        holder.timeLeft.setText("time has over " + date);

                        holder.status.setVisibility(View.GONE);
                        holder.timeOutStatus.setVisibility(View.VISIBLE);

                    }else {

                        holder.imageProfile.setImageDrawable(context.getDrawable(R.drawable.task_timer));

                        holder.timeLeftCardView.setCardBackgroundColor(context.getResources().getColor(R.color.darkGrey));
                        holder.timeLeft.setText(String.valueOf("Today at " + time));

                        holder.status.setVisibility(View.VISIBLE);
                        holder.timeOutStatus.setVisibility(View.GONE);

                    }

                }

            }

        }

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("uniqueID", uniqueID);
                editor.putString("title", title);
                editor.putString("leaderName", leaderName);
                editor.putString("leaderProfileImg", leaderProfileImg);
                editor.putString("coLeaderProfileImg", coLeaderProfileImg);
                editor.putString("time", time);
                editor.putString("date", date);
                editor.putString("status", status);
                editor.putString("completeDate", completeDate);
                editor.putString("timeLeft", holder.timeLeft.getText().toString());

                editor.apply();

                context.startActivity(new Intent(context, ProjectActivity.class)
                        .putExtra("uniqueID", uniqueID)
                        .putExtra("title", title)
                );

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        CircleImageView leaderProfile, coProfile, imageProfile;
        TextView title, leaderName, timeLeft, status, timeOutStatus, taskCompleteStaus;
        CardView timeLeftCardView;
        LinearLayout ll;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            leaderProfile = itemView.findViewById(R.id.leaderProfileImg);
            coProfile = itemView.findViewById(R.id.coLeaderProfileImg);
            title = itemView.findViewById(R.id.tv_title);
            leaderName = itemView.findViewById(R.id.tv_leaderName);
            timeLeft = itemView.findViewById(R.id.tv_timeLeft);
            status = itemView.findViewById(R.id.tv_status);
            timeOutStatus = itemView.findViewById(R.id.tv_still_status);
            taskCompleteStaus = itemView.findViewById(R.id.tv_complete_status);
            timeLeftCardView = itemView.findViewById(R.id.timeLeftCardView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            ll = itemView.findViewById(R.id.taskLL);

        }
    }
}
