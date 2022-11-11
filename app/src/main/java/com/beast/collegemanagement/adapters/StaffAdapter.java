package com.beast.collegemanagement.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.StaffModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffHolder> {

    List<StaffModel> list;
    Activity activity;
    BottomSheetDialog bottomSheetDialog;

    public StaffAdapter(List<StaffModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StaffHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_stafflist_layout, parent, false);

        return new StaffHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffHolder holder, int position) {
        Glide.with(activity).load(list.get(position).getProfilePic()).into(holder.profilePic);
        holder.userName.setText(list.get(position).getUsername());
        holder.position.setText(list.get(position).getPosition());

        holder.llStaffList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

    }

    private void showBottomSheet() {

        bottomSheetDialog = new BottomSheetDialog(activity);

        View view = LayoutInflater.from(activity).inflate(R.layout.bottomsheet_staff_select, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();

        view.findViewById(R.id.HOD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });view.findViewById(R.id.specotor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });view.findViewById(R.id.faculty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });view.findViewById(R.id.staff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class StaffHolder extends RecyclerView.ViewHolder {

    ImageView profilePic;
    TextView userName, position;
    LinearLayout llStaffList;

    public StaffHolder(@NonNull View itemView) {
        super(itemView);

        profilePic = itemView.findViewById(R.id.profilePicStaff);
        userName = itemView.findViewById(R.id.userName);
        position = itemView.findViewById(R.id.position);
        llStaffList = itemView.findViewById(R.id.llStaffList);

    }
}
