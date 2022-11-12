package com.beast.collegemanagement.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.StaffModel;
import com.bumptech.glide.Glide;

import java.util.List;


public class ObserverAdapter extends RecyclerView.Adapter<ObserverAdapter.ObserverHolder> {

    List<StaffModel> list;
    Activity activity;
    boolean isSelected = false;

    public ObserverAdapter(List<StaffModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ObserverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_observer_list, parent, false);

        return new ObserverHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObserverHolder holder, int position) {

        Glide.with(activity).load(list.get(position).getProfilePic()).into(holder.profilePic);
        holder.userName.setText(list.get(position).getFullName());
        holder.position.setText(list.get(position).getUsername());

        holder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.selectBtn.getText().toString().equals("select")){
//                    holder.selectBtn.setBackgroundColor(Color.TRANSPARENT);
                    holder.selectBtn.setBackgroundColor(activity.getResources().getColor(R.color.grey));
                    holder.selectBtn.setText("undo");
                }else {
                    holder.selectBtn.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                    holder.selectBtn.setText("select");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ObserverHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView userName, position;
        LinearLayout llStaffList;
        Button selectBtn;

        public ObserverHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePicStaff);
            userName = itemView.findViewById(R.id.userName);
            position = itemView.findViewById(R.id.position);
            llStaffList = itemView.findViewById(R.id.llStaffList);
            selectBtn = itemView.findViewById(R.id.selectBtn);

        }
    }

}
