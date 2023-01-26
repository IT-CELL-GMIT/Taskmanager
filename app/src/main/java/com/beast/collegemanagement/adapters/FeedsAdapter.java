package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.FeedsModel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsHolder> {

    List<FeedsModel> list;
    Context context;
    String activityName;

    public FeedsAdapter(List<FeedsModel> list, Context context, String activityName) {
        this.list = list;
        this.context = context;
        this.activityName = activityName;
    }

    @NonNull
    @Override
    public FeedsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_feeds_layout, parent, false);

        return new FeedsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedsHolder holder, int position) {

        if (activityName.equalsIgnoreCase("FeedsFragment")) {

            holder.optionsLL.setVisibility(View.VISIBLE);
            holder.optionsView.setVisibility(View.VISIBLE);

            Glide.with(context).load(list.get(position).getProfilePic()).into(holder.profilePic);
            holder.userName.setText(list.get(position).getUserName());
            holder.position.setText(list.get(position).getPosition());
            holder.feedText.setText(list.get(position).getFeedText());
            String[] splits = list.get(position).getTimeDate().split("xxx");
            holder.timeDate.setText(splits[0] + "  " + splits[1]);

        }else if (activityName.equalsIgnoreCase("HistoryFragment")){

            holder.optionsLL.setVisibility(View.GONE);
            holder.optionsView.setVisibility(View.GONE);

            Glide.with(context).load(list.get(position).getProfilePic()).into(holder.profilePic);
            holder.userName.setText(list.get(position).getUserName());
            holder.position.setText(list.get(position).getPosition());
            holder.feedText.setText(list.get(position).getFeedText());
            String[] splits = list.get(position).getTimeDate().split("xxx");
            holder.timeDate.setText(splits[0] + "  " + splits[1]);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FeedsHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;
        TextView userName, position, feedText, timeDate;
        LinearLayout optionsLL;
        View optionsView;

        public FeedsHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.image_profile);
            userName = itemView.findViewById(R.id.tv_name);
            position = itemView.findViewById(R.id.tv_desc);
            feedText = itemView.findViewById(R.id.tv_feedText);
            timeDate = itemView.findViewById(R.id.tv_timeDate);
            optionsLL = itemView.findViewById(R.id.optionsLL);
            optionsView = itemView.findViewById(R.id.optionsView);

        }
    }
}
