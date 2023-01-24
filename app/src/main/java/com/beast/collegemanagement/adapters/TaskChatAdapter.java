package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.R;
import com.beast.collegemanagement.TaskChatActivity;
import com.beast.collegemanagement.models.TaskChatModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskChatAdapter extends RecyclerView.Adapter<TaskChatAdapter.ChatHolder> {

    List<TaskChatModel> list;
    Context context;
    String activityName;

    public TaskChatAdapter(List<TaskChatModel> list, Context context, String activityName) {
        this.list = list;
        this.context = context;
        this.activityName = activityName;
    }

    @NonNull
    @Override
    public TaskChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_taskchat_layout, parent, false);

        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskChatAdapter.ChatHolder holder, int position) {

        holder.taskName.setText(list.get(position).getTaskName());

        holder.fullLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TaskChatActivity.class)
                        .putExtra("taskId", list.get(position).getTaskId())
                        .putExtra("taskName", list.get(position).getTaskName()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {

        LinearLayout fullLL;
        CircleImageView profileImg;
        TextView taskName;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            fullLL = itemView.findViewById(R.id.fullLL);
            profileImg = itemView.findViewById(R.id.image_profile);
            taskName = itemView.findViewById(R.id.tv_name);

        }
    }
}
