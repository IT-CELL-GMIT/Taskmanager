package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ParticipantsModel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantHolder> {

    List<ParticipantsModel> list;
    String activityName;
    Context context;

    public ParticipantsAdapter(List<ParticipantsModel> list, String activityName, Context context) {
        this.list = list;
        this.activityName = activityName;
        this.context = context;
    }

    @NonNull
    @Override
    public ParticipantsAdapter.ParticipantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_participants_layout, parent, false);

        return new ParticipantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantsAdapter.ParticipantHolder holder, int position) {

        Glide.with(context).load(list.get(position).getProfilePic()).into(holder.profilePic);
        holder.userName.setText(list.get(position).getUserName());
        holder.position.setText(list.get(position).getPosition());

        if (list.get(position).getIsSelected().equalsIgnoreCase("yes")){

            holder.selectBtn.setBackgroundColor(context.getResources().getColor(R.color.light));
            holder.selectBtn.setText("undo");

        }

        holder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.selectBtn.getText().toString().equalsIgnoreCase("select")){

                    Common.commonList.add(list.get(position).getUserName());
                    holder.selectBtn.setBackgroundColor(context.getResources().getColor(R.color.light));
                    holder.selectBtn.setText("undo");

                }else {

                    Common.commonList.remove(list.get(position).getUserName());
                    holder.selectBtn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.selectBtn.setText("select");

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ParticipantHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;
        TextView userName, position;
        Button selectBtn;

        public ParticipantHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePicStaff);
            userName = itemView.findViewById(R.id.userName);
            position = itemView.findViewById(R.id.position);
            selectBtn = itemView.findViewById(R.id.selectBtn);

        }
    }
}
