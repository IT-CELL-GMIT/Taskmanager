package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.ChatsActivity;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.ChatListModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {

    private List<ChatListModel> list;
    private Context context;

    public ChatListAdapter(List<ChatListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_chat_list,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        String currentDate = new SimpleDateFormat("dd-MM_yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        holder.tvName.setText(list.get(position).getFullName());
        holder.position.setText(list.get(position).getPosition());

        int nomsg = Integer.parseInt(list.get(position).getNoOfMsgs());
        if (nomsg > 9){
            holder.noOfMsgsShown.setVisibility(View.VISIBLE);
            holder.noOfMsgs.setText("9+");
        }else if(nomsg == 0){
            holder.noOfMsgsShown.setVisibility(View.GONE);
        }else {
            holder.noOfMsgs.setVisibility(View.VISIBLE);
            holder.noOfMsgs.setText(String.valueOf(nomsg));
        }

        if (list.get(position).getChatType().equalsIgnoreCase("PERSON")){
            holder.lastSeen.setVisibility(View.VISIBLE);
            String chatDate = list.get(position).getLastOnline();
            ///referrence 55-55-5555xxx55:55
            String splits[] = chatDate.split("xxx");
            if (splits[0].equalsIgnoreCase(currentDate)){
                holder.lastSeen.setText("today at " + splits[1]);
                holder.onlineIcon.setVisibility(View.GONE);
            }else {

                if (list.get(position).getLastOnline().equalsIgnoreCase("ONLINE")){
                    holder.lastSeen.setText("online");
                    holder.onlineIcon.setVisibility(View.VISIBLE);
                }else {

                    holder.onlineIcon.setVisibility(View.GONE);

                    String splits2[] = splits[0].split("-");
                    String splits3[] = currentDate.split("-");

                    if (!splits2[1].trim().equalsIgnoreCase(splits3[1].trim())) {
                        holder.lastSeen.setText("long ago");
                    } else {

                        int todayDt = Integer.parseInt(splits3[0]);
                        int lastSeenDt = Integer.parseInt(splits2[0]);

                        int diff = todayDt - lastSeenDt;

                        if (diff < 10) {
                            holder.lastSeen.setText(String.valueOf(diff) + " days ago");
                        } else {
                            holder.lastSeen.setText("long ago");
                        }

                    }
                }

            }
        }else {
            holder.lastSeen.setVisibility(View.GONE);
        }

        Glide.with(context).load(list.get(position).getProfilePic()).into(holder.profile);

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ChatsActivity.class)
                        .putExtra("userId", list.get(position).getUserId())
                        .putExtra("userName", list.get(position).getUserName())
                        .putExtra("fullName", list.get(position).getFullName())
                        .putExtra("profilePic", list.get(position).getProfilePic())
                        .putExtra("phoneNumber", list.get(position).getPhoneNumber())
                        .putExtra("eMail", list.get(position).geteMail())
                        .putExtra("lastOnline", list.get(position).getLastOnline())
                        .putExtra("position", list.get(position).getPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView tvName, position, noOfMsgs, lastSeen;
        private CircleImageView profile, onlineIcon;
        private LinearLayout ll;
        private RelativeLayout noOfMsgsShown;

        public Holder(@NonNull View itemView) {
            super(itemView);

            noOfMsgs = itemView.findViewById(R.id.tv_date);
            position = itemView.findViewById(R.id.tv_desc);
            tvName = itemView.findViewById(R.id.tv_name);
            profile = itemView.findViewById(R.id.image_profile);
            ll = itemView.findViewById(R.id.chatListLL);
            onlineIcon = itemView.findViewById(R.id.statusOnline);
            lastSeen = itemView.findViewById(R.id.lastSeen);
            noOfMsgsShown = itemView.findViewById(R.id.numberOfMSGShown);

        }
    }
}
