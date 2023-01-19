package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ObserverTaskModel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ObserverTaskAdapter extends RecyclerView.Adapter<ObserverTaskAdapter.observerHoler> {

    Context context;
    List<ObserverTaskModel> list;

    String userName,
            fullName,
            profilePic;

    public ObserverTaskAdapter(Context context, List<ObserverTaskModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ObserverTaskAdapter.observerHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_observ_layout, parent, false);

        return new observerHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObserverTaskAdapter.observerHoler holder, int position) {

        userName = list.get(position).getUserName();
        fullName = list.get(position).getFullName();
        profilePic = list.get(position).getProfilePic();

        Glide.with(context).load(profilePic).into(holder.profilePic);
        holder.fullName.setText(fullName);

        holder.fullLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.clickMark.getVisibility() == View.VISIBLE){

                    holder.clickMark.setVisibility(View.GONE);

                    Common.removeFromBoth(userName, profilePic);

                }else {

                    holder.clickMark.setVisibility(View.VISIBLE);
                    Common.addToCommonList(list.get(position).getUserName());
                    Common.addToCommonProfileList(list.get(position).getProfilePic());

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class observerHoler extends RecyclerView.ViewHolder {
        CircleImageView profilePic, clickMark;
        TextView fullName;
        LinearLayout fullLayout;

        public observerHoler(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePicImg);
            fullName = itemView.findViewById(R.id.userNameTV);
            fullLayout = itemView.findViewById(R.id.observerFullLL);
            clickMark = itemView.findViewById(R.id.clickMark);

        }
    }
}
