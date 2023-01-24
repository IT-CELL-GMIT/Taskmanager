package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.ChatsGroupActivity;
import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ChatListModel;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddGroupChatAdapter extends RecyclerView.Adapter<AddGroupChatAdapter.ChatHolder> {

    List<ChatListModel> list;
    Context context;
    String activityName;

    public AddGroupChatAdapter(List<ChatListModel> list, Context context, String activityName) {
        this.list = list;
        this.context = context;
        this.activityName = activityName;
    }

    @NonNull
    @Override
    public AddGroupChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_groupchat_layout, parent, false);

        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddGroupChatAdapter.ChatHolder holder, int position) {



        if (activityName.equals("AddGroupChatActivity")) {

            Glide.with(context).load(list.get(position).getProfilePic()).into(holder.profileImg);
            holder.userName.setText(list.get(position).getUserName());
            holder.position.setText(list.get(position).getPosition());

            holder.fullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (holder.checkBtn.getVisibility() == View.VISIBLE) {

                        holder.checkBtn.setVisibility(View.GONE);
                        Common.commonList.remove(list.get(position).getUserName());

                    } else {

                        holder.checkBtn.setVisibility(View.VISIBLE);
                        Common.addToCommonList(list.get(position).getUserName());

                    }

                }
            });

        } else if (activityName.equals("AddGroupActivity")){

            Glide.with(context).load(list.get(position).getProfilePic()).into(holder.profileImg);
            holder.userName.setText(list.get(position).getUserName());
            holder.position.setText(list.get(position).getPosition());

        }else if (activityName.equals("GroupChatFragment")){

            Glide.with(context).load(list.get(position).getProfilePic()).into(holder.profileImg);
            holder.userName.setText(list.get(position).getFullName());
            holder.position.setVisibility(View.GONE);

            holder.fullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ChatsGroupActivity.class)
                            .putExtra("userName", list.get(position).getUserName())
                            .putExtra("fullName", list.get(position).getFullName())
                            .putExtra("profilePic", list.get(position).getProfilePic()));
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {

        LinearLayout fullLayout;
        CircleImageView profileImg, checkBtn;
        TextView userName, position;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            fullLayout = itemView.findViewById(R.id.chatListLL);
            profileImg = itemView.findViewById(R.id.image_profile);
            userName = itemView.findViewById(R.id.tv_name);
            position = itemView.findViewById(R.id.tv_desc);
            checkBtn = itemView.findViewById(R.id.selectCheck);

        }
    }
}
