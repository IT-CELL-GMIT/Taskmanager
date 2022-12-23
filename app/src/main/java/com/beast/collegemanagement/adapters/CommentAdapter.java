package com.beast.collegemanagement.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.collegemanagement.Common;
import com.beast.collegemanagement.R;
import com.beast.collegemanagement.models.CommentModel;
import com.bumptech.glide.Glide;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    List<CommentModel> list;
    Context context;

    String profilePic,
            fullName,
            time,
            content,
    type,
    link;

    String todayTime, todayDate;
    String commentTime, commentDate;

    public CommentAdapter(List<CommentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_comment_list, parent, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {

        profilePic = list.get(position).getProfilePic();
        fullName = list.get(position).getFullName();
        time = list.get(position).getTimeDate();
        content = list.get(position).getContent();
        type = list.get(position).getType();
        link = list.get(position).getLink();

        todayTime = Common.getTIme();
        todayDate = Common.getDate();

        Glide.with(context).load(profilePic).into(holder.profilePic);

        String [] splits = time.split("xxx");

        commentTime = splits[0];
        commentDate = splits[1];

        if (type.equalsIgnoreCase("TEXT")){

            holder.imageCard.setVisibility(View.GONE);
            holder.content.setText(content);
            holder.fullName.setText(fullName);

            if (commentDate.equalsIgnoreCase(todayDate)){
                holder.time.setText(commentTime);
            }else {
                holder.time.setText(commentDate);
            }

        }else if (type.equalsIgnoreCase("IMAGE")){

            holder.textLL.setVisibility(View.GONE);

            holder.contentImg.setImageBitmap(Common.IMAGE_BITMAP);

            holder.fullNameImg.setText(fullName);

            if (commentDate.equalsIgnoreCase(todayDate)){
                holder.timeImg.setText(commentTime);
            }else {
                holder.timeImg.setText(commentDate);
            }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;
        TextView fullName, time, content, fullNameImg, timeImg;
        CardView imageCard;
        LinearLayout textLL;
        ImageView contentImg;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            fullName = itemView.findViewById(R.id.fullName);
            time = itemView.findViewById(R.id.time);
            content = itemView.findViewById(R.id.content);
            imageCard = itemView.findViewById(R.id.card_image);
            textLL = itemView.findViewById(R.id.textLL);
            contentImg = itemView.findViewById(R.id.contentImg);
            fullNameImg = itemView.findViewById(R.id.fullNameImg);
            timeImg = itemView.findViewById(R.id.timeImg);

        }
    }
}
