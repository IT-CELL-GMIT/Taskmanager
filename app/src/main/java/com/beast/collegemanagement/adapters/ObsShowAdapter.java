package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ObsShowModel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ObsShowAdapter extends RecyclerView.Adapter<ObsShowAdapter.ObsHolder> {

    Context context;
    List<ObsShowModel> list;

    public ObsShowAdapter(Context context, List<ObsShowModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ObsShowAdapter.ObsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_singlesmall_profile, parent, false);

        return new ObsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObsShowAdapter.ObsHolder holder, int position) {

        Glide.with(context).load(list.get(position).getProfilePic()).into(holder.profilePic);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ObsHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;

        public ObsHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePicImg);

        }
    }
}
